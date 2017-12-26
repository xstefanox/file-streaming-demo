package io.github.xstefanox.demo.filestreaming.processor;

import io.github.xstefanox.demo.filestreaming.exception.FileProcessingException;
import io.github.xstefanox.demo.filestreaming.exception.LineProcessingException;
import io.github.xstefanox.demo.filestreaming.listener.ErrorListener;
import io.github.xstefanox.demo.filestreaming.model.Line;
import io.github.xstefanox.demo.filestreaming.util.FileObjectStreams;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

public class DefaultFileProcessor implements FileProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultFileProcessor.class);

    private final LineProcessor lineProcessor;
    private final Integer nThreads;
    private final List<ErrorListener> errorListeners = new ArrayList<>();

    public DefaultFileProcessor(final LineProcessor lineProcessor, final Integer nThreads) {

        requireNonNull(lineProcessor, "lineProcessor must not be null");
        requireNonNull(nThreads, "nThreads must not be null");

        if (nThreads < 1) {
            throw new IllegalArgumentException("the number of threads must be greater than 0");
        }

        this.lineProcessor = lineProcessor;
        this.nThreads = nThreads;
    }

    public void registerErrorListener(final ErrorListener errorListener) {
        errorListeners.add(errorListener);
    }

    @Override
    public void process(final FileObject path) throws FileProcessingException {

        LOGGER.debug("creating executor thread pool, size {}", nThreads);

        final ExecutorService executor = Executors.newFixedThreadPool(nThreads, new ProcessorThreadFactory("DefaultFileProcessor"));

        try (Stream<String> lines = FileObjectStreams.lines(path)) {

            final AtomicInteger lineCounter = new AtomicInteger();

            final Phaser phaser = new Phaser(1);

            lines.forEach(lineContent -> {

                phaser.register();

                final Runnable runnable = () -> {

                    final Line line = new Line(lineCounter.incrementAndGet(), lineContent);

                    LOGGER.debug("processing line {}", line);

                    try {
                        lineProcessor.process(line);
                        LOGGER.debug("line {} processed", line.number);
                    } catch (LineProcessingException e) {
                        errorListeners.forEach(errorListener -> errorListener.notifyException(e));
                    } finally {
                        phaser.arrive();
                    }
                };

                executor.execute(runnable);
            });

            phaser.arriveAndAwaitAdvance();

            executor.shutdown();

        } catch (FileSystemException e) {
            throw new FileProcessingException(path, e);
        }
    }
}
