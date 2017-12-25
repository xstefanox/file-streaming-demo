package io.github.xstefanox.demo.filestreaming.exception;

import java.nio.file.Path;

public class FileProcessingException extends Exception {

    public FileProcessingException(final Path path, final Throwable cause) {
        super("cannot process file " + path, cause);
    }
}
