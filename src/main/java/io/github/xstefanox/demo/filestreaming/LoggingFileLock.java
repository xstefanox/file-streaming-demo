package io.github.xstefanox.demo.filestreaming;

import io.github.xstefanox.demo.filestreaming.exception.CannotAcquireLockException;
import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.nio.file.StandardOpenOption.WRITE;

public class LoggingFileLock implements Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFileLock.class);

    private final Path path;
    private final FileLock lock;

    public LoggingFileLock(final Path path) {

        LOGGER.debug("acquiring lock on {}", path);

        try {

            @SuppressWarnings("squid:S2095")
            // it is up to the caller to close this resource by closing the returned stream
            final FileLock fileLock = FileChannel.open(path, WRITE).tryLock();

            if (fileLock == null) {
                throw new CannotAcquireLockException(path);
            }

            this.lock = fileLock;
            this.path = path;

        } catch (IOException e) {
            throw new CannotAcquireLockException(path, e);
        }
    }

    @Override
    public void close() throws IOException {

        LOGGER.debug("releasing lock on {}", path);

        lock.close();
    }
}
