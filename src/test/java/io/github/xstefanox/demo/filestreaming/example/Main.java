package io.github.xstefanox.demo.filestreaming.example;

import io.github.xstefanox.demo.filestreaming.exception.FileProcessingException;
import io.github.xstefanox.demo.filestreaming.listener.FileWriterErrorListener;
import java.net.URI;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.cache.NullFilesCache;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger("FileStreamingDemo");

    private Main() {
        throw new AssertionError("main class must not be instantiated");
    }

    public static void main(final String... args) throws FileProcessingException, FileSystemException {

        final String cwd = System.getProperty("user.dir");

        final URI uncompressedSftpPath = URI.create("sftp://foo:pass@localhost:2222/test1.txt");
        final URI compressedSftpPath = URI.create("gz:sftp://foo:pass@localhost:2222/test.txt.gz");
        final URI compressedLocalPath = URI.create("gz:file://" + cwd + "/data/test.txt.gz");
        final URI uncompressedLocalPath = URI.create("file://" + cwd + "/data/test.txt");
        final URI uncompressedLocalRelativePath = URI.create(cwd + "/data/test.txt");

        final URI errorsFile = URI.create("file://" + cwd + "/data/errors.txt");

        final StandardFileSystemManager fileSystemManager = new StandardFileSystemManager();

        try {

            fileSystemManager.setFilesCache(new NullFilesCache());
            fileSystemManager.init();

            final LoggingLineProcessor loggingLineProcessor = new LoggingLineProcessor();
            final LoggingFileProcessor loggingFileProcessor1 = new LoggingFileProcessor(loggingLineProcessor, 1, LOGGER);

            try (final FileObject fileObject = fileSystemManager.resolveFile(compressedSftpPath)) {
                loggingFileProcessor1.process(fileObject);
            }

            try (final FileObject fileObject = fileSystemManager.resolveFile(uncompressedSftpPath)) {
                loggingFileProcessor1.process(fileObject);
            }

            try (final FileWriterErrorListener fileWriterErrorListener = new FileWriterErrorListener(fileSystemManager.resolveFile(errorsFile))) {

                final LoggingErrorListener loggingErrorListener = new LoggingErrorListener();

                final RandomErrorLineProcessor randomErrorLineProcessor = new RandomErrorLineProcessor();
                final LoggingFileProcessor loggingFileProcessor2 = new LoggingFileProcessor(randomErrorLineProcessor, 1, LOGGER);
                loggingFileProcessor2.registerErrorListener(loggingErrorListener);
                loggingFileProcessor2.registerErrorListener(fileWriterErrorListener);

                try (final FileObject fileObject = fileSystemManager.resolveFile(compressedLocalPath)) {
                    loggingFileProcessor2.process(fileObject);
                }

                try (final FileObject fileObject = fileSystemManager.resolveFile(uncompressedLocalPath)) {
                    loggingFileProcessor2.process(fileObject);
                }

                try (final FileObject fileObject = fileSystemManager.resolveFile(uncompressedLocalRelativePath)) {
                    loggingFileProcessor2.process(fileObject);
                }
            }

        } finally {

            fileSystemManager.close();
        }
    }
}
