package io.github.xstefanox.demo.filestreaming.processor;

import io.github.xstefanox.demo.filestreaming.listener.FileWriterErrorListener;

public class FileProcessorBuilder {

    private LineProcessor lineProcessor;
    private Integer threads;
    private FileWriterErrorListener fileWriterErrorListener;

    public FileProcessorBuilder processLinesWith(final LineProcessor lineProcessor) {
        this.lineProcessor = lineProcessor;
        return this;
    }

    public FileProcessorBuilder usingThreads(final Integer threads) {
        this.threads = threads;
        return this;
    }

    public FileProcessorBuilder writingErrorsUsing(final FileWriterErrorListener fileWriterErrorListener) {
        this.fileWriterErrorListener = fileWriterErrorListener;
        return this;
    }

    public FileProcessor build() {

        final DefaultFileProcessor defaultFileProcessor = new DefaultFileProcessor(lineProcessor, threads);
        defaultFileProcessor.registerErrorListener(fileWriterErrorListener);

        return defaultFileProcessor;
    }
}
