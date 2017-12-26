package io.github.xstefanox.demo.filestreaming.listener;

import io.github.xstefanox.demo.filestreaming.exception.CannotOpenErrorsFileException;
import io.github.xstefanox.demo.filestreaming.exception.LineProcessingException;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.PrintWriter;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

public class FileWriterErrorListener implements ErrorListener, Closeable {

    private static final Character FIELD_SEPARATOR = '\t';

    private final PrintWriter printWriter;

    public FileWriterErrorListener(final FileObject fileObject) {
        try {
            this.printWriter = new PrintWriter(new BufferedOutputStream(fileObject.getContent().getOutputStream()));
        } catch (FileSystemException e) {
            throw new CannotOpenErrorsFileException(fileObject, e);
        }
    }

    @Override
    public void notifyException(final LineProcessingException e) {
        printWriter.print(e.getLine().number);
        printWriter.print(FIELD_SEPARATOR);
        printWriter.print(e.getCause().getMessage());
        printWriter.print(FIELD_SEPARATOR);
        printWriter.print(e.getLine().content);
        printWriter.println();
    }

    @Override
    public void close() {
        printWriter.close();
    }
}
