package io.github.xstefanox.demo.filestreaming.exception;

import io.github.xstefanox.demo.filestreaming.model.Line;

import static java.util.Objects.requireNonNull;

public class LineProcessingException extends Exception {

    private final transient Line line;

    public LineProcessingException(final Line line, final Throwable cause) {

        super("cannot process line " + line, cause);

        requireNonNull(line, "line must not be null");

        this.line = line;
    }

    public Line getLine() {
        return line;
    }
}
