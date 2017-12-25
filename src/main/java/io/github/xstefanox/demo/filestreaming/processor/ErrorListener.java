package io.github.xstefanox.demo.filestreaming.processor;

import io.github.xstefanox.demo.filestreaming.exception.LineProcessingException;

public interface ErrorListener {

    void notifyException(LineProcessingException e);
}
