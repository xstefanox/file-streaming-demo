package io.github.xstefanox.demo.filestreaming;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {

    private static final String DATA_DIR = "data";

    private Main() {
        throw new AssertionError("main class must not be instantiated");
    }

    public static void main(final String... args) throws IOException {

        final Path path = Paths.get(System.getProperty("user.dir"), DATA_DIR, "test.txt.gz");

        try (final Stream<String> lines = GZIPFiles.isGZipped(path) ? GZIPFiles.lines(path) : Files.lines(path)) {
            lines.forEach(System.out::println);
        }
    }
}
