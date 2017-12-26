package io.github.xstefanox.demo.filestreaming.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileObjectStreamsTest {

    @Mock
    private FileObject fileObject;

    @Mock
    private FileContent fileContent;

    @Spy
    private InputStream emptyInputStream = new ByteArrayInputStream("".getBytes());

    @Spy
    private InputStream oneLineInputStream = new ByteArrayInputStream("TEST".getBytes());

    @Spy
    private InputStream multipleLinesInputStream = new ByteArrayInputStream("TEST1\nTEST2".getBytes());

    @Before
    public void setUp() throws FileSystemException {
        when(fileObject.getContent()).thenReturn(fileContent);
    }

    @Test
    public void emptyInputStreamShouldProduceNoLines() throws IOException {

        when(fileContent.getInputStream()).thenReturn(emptyInputStream);

        final List<String> lines = FileObjectStreams.lines(fileObject).collect(toList());

        assertThat("empty input stream should produce no lines", lines, empty());
    }

    @Test
    public void oneLineInpuStreamShouldProduce1Line() throws FileSystemException {

        when(fileContent.getInputStream()).thenReturn(oneLineInputStream);

        final List<String> lines = FileObjectStreams.lines(fileObject).collect(toList());

        assertThat("one line input stream should produce one line", lines, hasSize(1));
        assertThat("line content should be the same of input stream", lines.get(0), equalTo("TEST"));
    }

    @Test
    public void inputStreamShouldBeClosedOnStreamClose() throws IOException {

        when(fileContent.getInputStream()).thenReturn(oneLineInputStream);

        try (final Stream<String> lines = FileObjectStreams.lines(fileObject)) {
            lines.forEach(System.out::println);
        }

        verify(oneLineInputStream).close();
    }

    @Test
    public void linesShouldBeSplittedByLineFeed() throws FileSystemException {

        when(fileContent.getInputStream()).thenReturn(multipleLinesInputStream);

        final List<String> lines = FileObjectStreams.lines(fileObject).collect(toList());

        assertThat("one line input stream should produce multiple lines", lines, hasSize(2));
        assertThat("line content should be the same of input stream", lines, equalTo(asList("TEST1", "TEST2")));
    }
}
