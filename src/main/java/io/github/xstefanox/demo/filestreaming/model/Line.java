package io.github.xstefanox.demo.filestreaming.model;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public final class Line {

    public final Integer number;

    public final String content;

    public Line(final Integer number, final String content) {

        requireNonNull(number, "number must not be null");
        requireNonNull(content, "content must not be null");

        this.number = number;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Line line1 = (Line) o;

        return Objects.equals(number, line1.number) &&
                Objects.equals(content, line1.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, content);
    }

    @Override
    public String toString() {
        return "Line{" +
                "number=" + number +
                ", content='" + content + '\'' +
                '}';
    }
}
