package io.github.xstefanox.demo.filestreaming.exception;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

public class UnreadableInputDirectory extends FileSystemException {

    public UnreadableInputDirectory(final FileObject fileObject) {
        super("directory " + fileObject + " is not readable");
    }
}
