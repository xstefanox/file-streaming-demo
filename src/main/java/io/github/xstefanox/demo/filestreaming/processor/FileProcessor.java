package io.github.xstefanox.demo.filestreaming.processor;

import io.github.xstefanox.demo.filestreaming.exception.FileProcessingException;
import java.nio.file.Path;

public interface FileProcessor {

    void process(Path path) throws FileProcessingException;
}
