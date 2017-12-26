package io.github.xstefanox.demo.filestreaming.example;

import io.github.xstefanox.demo.filestreaming.exception.FileProcessingException;
import io.github.xstefanox.demo.filestreaming.processor.DefaultFileProcessor;
import io.github.xstefanox.demo.filestreaming.processor.LineProcessor;
import org.apache.commons.vfs2.FileObject;
import org.slf4j.Logger;

public class LoggingFileProcessor extends DefaultFileProcessor {

    private final Logger logger;

    public LoggingFileProcessor(final LineProcessor lineProcessor, final Integer nThreads, Logger logger) {
        super(lineProcessor, nThreads);
        this.logger = logger;
    }

    @Override
    public void process(FileObject path) throws FileProcessingException {
        logger.info("processing file {}", path);
        super.process(path);
    }
}
