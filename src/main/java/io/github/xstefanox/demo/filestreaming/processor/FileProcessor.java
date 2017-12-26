package io.github.xstefanox.demo.filestreaming.processor;

import io.github.xstefanox.demo.filestreaming.exception.FileProcessingException;
import org.apache.commons.vfs2.FileObject;

public interface FileProcessor {

    void process(FileObject path) throws FileProcessingException;
}
