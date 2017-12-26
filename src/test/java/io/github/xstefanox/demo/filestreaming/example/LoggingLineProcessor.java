package io.github.xstefanox.demo.filestreaming.example;

import io.github.xstefanox.demo.filestreaming.exception.LineProcessingException;
import io.github.xstefanox.demo.filestreaming.model.Line;
import io.github.xstefanox.demo.filestreaming.processor.LineProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingLineProcessor implements LineProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingLineProcessor.class);

    @Override
    public void process(final Line line) throws LineProcessingException {
        LOGGER.info("processing line {}", line);
    }
}
