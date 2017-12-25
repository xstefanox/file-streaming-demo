package io.github.xstefanox.demo.filestreaming.processor;

import io.github.xstefanox.demo.filestreaming.exception.LineProcessingException;
import io.github.xstefanox.demo.filestreaming.model.Line;

public interface LineProcessor {

    void process(Line line) throws LineProcessingException;
}
