package io.github.xstefanox.demo.filestreaming.processor;

import io.github.xstefanox.demo.filestreaming.exception.FileProcessingException;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingFileProcessor extends AbstractFileProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFileProcessor.class);

    public LoggingFileProcessor(final LineProcessor lineProcessor) {
        super(lineProcessor);
    }

    @Override
    public void process(final Path path) throws FileProcessingException {

        LOGGER.info("processing file {}", path);

        super.process(path);
    }
}
