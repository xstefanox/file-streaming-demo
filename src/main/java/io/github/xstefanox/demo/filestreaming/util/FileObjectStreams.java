package io.github.xstefanox.demo.filestreaming.util;

import io.github.xstefanox.demo.filestreaming.exception.ReaderCloseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

public final class FileObjectStreams {

    private FileObjectStreams() {
        throw new AssertionError("this utility class must no be instantiated");
    }

    public static Stream<String> lines(final FileObject fileObject) throws FileSystemException {

        final BufferedReader reader = new BufferedReader(new InputStreamReader(fileObject.getContent().getInputStream()));

        return reader.lines().onClose(() -> {
            try {
                reader.close();
            } catch (final IOException e) {
                throw new ReaderCloseException(e);
            }
        });
    }
}
