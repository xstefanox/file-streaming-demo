package io.github.xstefanox.demo.filestreaming.listener;

import io.github.xstefanox.demo.filestreaming.exception.LineProcessingException;
import io.github.xstefanox.demo.filestreaming.model.Line;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileWriterErrorListenerTest {

    private static final Integer LINE_NUMBER = 1;

    private static final String LINE_CONTENT = "LINE_CONTENT";

    private static final String EXCEPTION_MESSAGE = "EXCEPTION_MESSAGE";

    private static final String EXPECTED_FILE_CONTENT = LINE_NUMBER + "\t" + EXCEPTION_MESSAGE + "\t" + LINE_CONTENT + System.getProperty("line.separator");

    @Mock
    private FileObject fileObject;

    @Mock
    private FileContent fileContent;

    private FileWriterErrorListener fileWriterErrorListener;

    @Spy
    private ByteArrayOutputStream out = new ByteArrayOutputStream();

    @Before
    public void setUp() throws FileSystemException {

        when(fileObject.getContent()).thenReturn(fileContent);

        when(fileContent.getOutputStream()).thenReturn(out);

        fileWriterErrorListener = new FileWriterErrorListener(fileObject);
    }

    @Test
    public void errorsShouldBeWrittenToFile() {

        final Line line = new Line(LINE_NUMBER, LINE_CONTENT);
        final Exception exception = new RuntimeException(EXCEPTION_MESSAGE);
        final LineProcessingException lineProcessingException = new LineProcessingException(line, exception);

        fileWriterErrorListener.notifyException(lineProcessingException);
        fileWriterErrorListener.close();

        assertThat("the file should contain 1 line", new String(out.toByteArray()), equalTo(EXPECTED_FILE_CONTENT));
    }

    @Test
    public void theFileShouldBeClosedOnCompletion() throws IOException {

        final Line line = new Line(LINE_NUMBER, LINE_CONTENT);
        final Exception exception = new RuntimeException(EXCEPTION_MESSAGE);
        final LineProcessingException lineProcessingException = new LineProcessingException(line, exception);

        fileWriterErrorListener.notifyException(lineProcessingException);
        fileWriterErrorListener.close();

        verify(out).close();
    }

    @Test
    public void theListenerShouldBeAutocloseable() {

        assertThat("the listener should be autocloseable", fileWriterErrorListener, instanceOf(AutoCloseable.class));
    }
}
