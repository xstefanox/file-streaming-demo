package io.github.xstefanox.demo.filestreaming;

import io.github.xstefanox.demo.filestreaming.exception.ReaderCloseException;
import io.github.xstefanox.demo.filestreaming.exception.UncompressedFileException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

import static org.apache.commons.compress.compressors.CompressorStreamFactory.GZIP;

public final class GZIPFiles {

    private static final CompressorStreamFactory COMPRESSOR_STREAM_FACTORY = new CompressorStreamFactory();

    private GZIPFiles() {
        throw new AssertionError("this utility  class must not be instantiated");
    }

    private static CompressorInputStream open(final Path path) throws IOException, CompressorException {
        return COMPRESSOR_STREAM_FACTORY.createCompressorInputStream(GZIP, Files.newInputStream(path));
    }

    public static Boolean isGZipped(final Path path) throws IOException {
        try (@SuppressWarnings("unused") final CompressorInputStream gzipIn = open(path)) {
            return true;
        } catch (CompressorException e) {
            return false;
        }
    }

    public static Stream<String> lines(final Path path) throws IOException {

        final InputStream is;

        try {
            is = open(path);
        } catch (CompressorException e) {
            throw new UncompressedFileException(path);
        }

        @SuppressWarnings("squid:S2095")   // it is up to the caller to close this resource by closing the returned stream
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        return reader.lines().onClose(() -> {
            try {
                reader.close();
            } catch (final IOException e) {
                throw new ReaderCloseException(e);
            }
        });
    }
}
