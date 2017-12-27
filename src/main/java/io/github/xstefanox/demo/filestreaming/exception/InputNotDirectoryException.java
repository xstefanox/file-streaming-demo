package io.github.xstefanox.demo.filestreaming.exception;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

public class InputNotDirectoryException extends FileSystemException {

    public InputNotDirectoryException(final FileObject baseDirectory) {
        super("path " + baseDirectory + " is not a directory");
    }
}
