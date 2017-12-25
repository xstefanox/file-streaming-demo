package io.github.xstefanox.demo.filestreaming.exception;

import java.nio.file.Path;

public class UncompressedFileException extends RuntimeException {

    public UncompressedFileException(final Path path) {
        super("file " + path + " is not compressed");
    }
}
