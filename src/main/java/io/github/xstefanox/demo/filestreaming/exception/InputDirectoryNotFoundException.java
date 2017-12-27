package io.github.xstefanox.demo.filestreaming.exception;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

public class InputDirectoryNotFoundException extends FileSystemException {

    public InputDirectoryNotFoundException(final FileObject fileObject) {
        super("directory " + fileObject + " not found");
    }
}
