package io.github.xstefanox.demo.filestreaming.example;

import io.github.xstefanox.demo.filestreaming.exception.LineProcessingException;
import io.github.xstefanox.demo.filestreaming.model.Line;
import java.util.Random;

import static java.util.UUID.randomUUID;

public class RandomErrorLineProcessor extends LoggingLineProcessor {

    private final Random random = new Random();

    @Override
    public void process(final Line line) throws LineProcessingException {
        super.process(line);

        if (random.nextBoolean()) {
            throw new LineProcessingException(line, new RuntimeException("strange error: " + randomUUID()));
        }
    }
}
