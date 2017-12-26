package io.github.xstefanox.demo.filestreaming.model;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class LineTest {

    @Test
    public void linesHavingSameNumberAndContentShouldBeEqual() {

        final Line line1 = new Line(1, "CONTENT");
        final Line line2 = new Line(1, "CONTENT");

        assertThat("lines having the same number and content should be equal", line1, equalTo(line2));
    }

    @Test
    public void linesHavingDifferentNumberShouldBeDifferent() {

        final Line line1 = new Line(1, "CONTENT");
        final Line line2 = new Line(2, "CONTENT");

        assertThat("lines having different number should be different", line1, not(equalTo(line2)));
    }

    @Test
    public void linesHavingDifferentContentShouldBeDifferent() {

        final Line line1 = new Line(1, "CONTENT_1");
        final Line line2 = new Line(1, "CONTENT_2");

        assertThat("lines having different number should be different", line1, not(equalTo(line2)));
    }

    @Test
    public void linesHavingSameNumberAndContentShouldHaveTheSameHash() {

        final Line line1 = new Line(1, "CONTENT");
        final Line line2 = new Line(1, "CONTENT");

        assertThat("lines having the same number and content should be equal", line1.hashCode(), equalTo(line2.hashCode()));
    }

    @Test
    public void linesHavingDifferentNumberShouldHaveDifferentHash() {

        final Line line1 = new Line(1, "CONTENT");
        final Line line2 = new Line(2, "CONTENT");

        assertThat("lines having different number should be different", line1.hashCode(), not(equalTo(line2.hashCode())));
    }

    @Test
    public void linesHavingDifferentContentShouldHaveDifferentHash() {

        final Line line1 = new Line(1, "CONTENT_1");
        final Line line2 = new Line(1, "CONTENT_2");

        assertThat("lines having different number should be different", line1.hashCode(), not(equalTo(line2.hashCode())));
    }
}
