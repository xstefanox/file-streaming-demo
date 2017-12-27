package io.github.xstefanox.demo.filestreaming.exception;

import java.net.URISyntaxException;

public class InvalidURIException extends RuntimeException {

    public InvalidURIException(final URISyntaxException e) {
        super("invalid uri", e);
    }
}
