package io.github.xstefanox.demo.filestreaming.exception;

import java.io.IOException;
import java.nio.file.Path;

public class CannotAcquireLockException extends RuntimeException {

    public CannotAcquireLockException(final Path path) {
        super("cannot acquire lock on " + path);
    }

    public CannotAcquireLockException(final Path path, final IOException e) {
        super("cannot acquire lock on " + path, e);
    }
}
