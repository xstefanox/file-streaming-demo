package io.github.xstefanox.demo.filestreaming.listener;

import io.github.xstefanox.demo.filestreaming.exception.LineProcessingException;

public interface ErrorListener {

    void notifyException(LineProcessingException e);
}
