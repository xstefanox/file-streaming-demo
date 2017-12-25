package io.github.xstefanox.demo.filestreaming.exception;

public class ReaderCloseException extends RuntimeException {

    public ReaderCloseException(Throwable cause) {
        super("cannot close buffered reader", cause);
    }
}
