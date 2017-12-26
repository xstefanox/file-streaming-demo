package io.github.xstefanox.demo.filestreaming.exception;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

public class CannotOpenErrorsFileException extends RuntimeException {

    public CannotOpenErrorsFileException(final FileObject fileObject, final FileSystemException e) {
        super("cannot open errors file " + fileObject, e);
    }
}
