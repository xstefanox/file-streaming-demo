package io.github.xstefanox.demo.filestreaming;

import io.github.xstefanox.demo.filestreaming.exception.FileProcessingException;
import io.github.xstefanox.demo.filestreaming.processor.LoggingErrorListener;
import io.github.xstefanox.demo.filestreaming.processor.LoggingFileProcessor;
import io.github.xstefanox.demo.filestreaming.processor.LoggingLineProcessor;
import io.github.xstefanox.demo.filestreaming.processor.RandomErrorLineProcessor;
import java.nio.file.Paths;

public class Main {

    private static final String DATA_DIR = "data";

    private Main() {
        throw new AssertionError("main class must not be instantiated");
    }

    public static void main(final String... args) throws FileProcessingException {

        final String cwd = System.getProperty("user.dir");
        final LoggingLineProcessor loggingLineProcessor = new LoggingLineProcessor();
        final LoggingFileProcessor loggingFileProcessor1 = new LoggingFileProcessor(loggingLineProcessor);

        loggingFileProcessor1.process(Paths.get(cwd, DATA_DIR, "test.txt.gz"));
        loggingFileProcessor1.process(Paths.get(cwd, DATA_DIR, "test.txt"));

        final RandomErrorLineProcessor randomErrorLineProcessor = new RandomErrorLineProcessor();
        final LoggingErrorListener loggingErrorListener = new LoggingErrorListener();
        final LoggingFileProcessor loggingFileProcessor2 = new LoggingFileProcessor(randomErrorLineProcessor);
        loggingFileProcessor2.registerErrorListener(loggingErrorListener);

        loggingFileProcessor2.process(Paths.get(cwd, DATA_DIR, "test.txt.gz"));
    }
}
