package io.github.xstefanox.demo.filestreaming.processor;

import io.github.xstefanox.demo.filestreaming.GZIPFiles;
import io.github.xstefanox.demo.filestreaming.exception.FileProcessingException;
import io.github.xstefanox.demo.filestreaming.exception.LineProcessingException;
import io.github.xstefanox.demo.filestreaming.model.Line;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public abstract class AbstractFileProcessor implements FileProcessor {

    private final LineProcessor lineProcessor;
    private final List<ErrorListener> errorListeners = new ArrayList<>();

    public AbstractFileProcessor(final LineProcessor lineProcessor) {
        this.lineProcessor = lineProcessor;
    }

    public void registerErrorListener(final ErrorListener errorListener) {
        errorListeners.add(errorListener);
    }

    @Override
    public void process(final Path path) throws FileProcessingException {

        try (final Stream<String> lines = GZIPFiles.isGZipped(path) ? GZIPFiles.lines(path) : Files.lines(path)) {

            final AtomicInteger lineCounter = new AtomicInteger();

            lines.map(line -> new Line(lineCounter.incrementAndGet(), line))
                    .forEach(line -> {
                        try {
                            lineProcessor.process(line);
                        } catch (LineProcessingException e) {
                            errorListeners.forEach(errorListener -> errorListener.notifyException(e));
                        }
                    });

        } catch (IOException e) {
            throw new FileProcessingException(path, e);
        }
    }
}
