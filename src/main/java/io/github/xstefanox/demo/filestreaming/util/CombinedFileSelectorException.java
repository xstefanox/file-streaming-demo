package io.github.xstefanox.demo.filestreaming.util;

import org.apache.commons.vfs2.FileSelectInfo;
import org.apache.commons.vfs2.FileSelector;

public class CombinedFileSelectorException extends RuntimeException {

    public CombinedFileSelectorException(final FileSelectInfo fileInfo, final FileSelector fileSelector, final Exception e) {
        super("error filtering file " + fileInfo.getFile() + " with selector " + fileSelector, e);
    }
}
