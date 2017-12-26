package io.github.xstefanox.demo.filestreaming.exception;

import org.apache.commons.vfs2.FileObject;

public class FileProcessingException extends Exception {


    public FileProcessingException(final FileObject path, final Throwable cause) {
        super("cannot process file " + path, cause);
    }
}
