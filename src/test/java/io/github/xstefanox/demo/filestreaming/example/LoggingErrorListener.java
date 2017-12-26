package io.github.xstefanox.demo.filestreaming.example;

import io.github.xstefanox.demo.filestreaming.exception.LineProcessingException;
import io.github.xstefanox.demo.filestreaming.listener.ErrorListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingErrorListener implements ErrorListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingErrorListener.class);

    @Override
    public void notifyException(final LineProcessingException e) {
        LOGGER.warn("error processing line {} : {}", e.getLine(), e.getCause().getMessage());
    }
}
