package io.github.xstefanox.demo.filestreaming.processor;

import io.github.xstefanox.demo.filestreaming.exception.FileProcessingException;
import io.github.xstefanox.demo.filestreaming.exception.LineProcessingException;
import io.github.xstefanox.demo.filestreaming.listener.ErrorListener;
import io.github.xstefanox.demo.filestreaming.model.Line;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultFileProcessorTest {

    @Mock
    private LineProcessor lineProcessor;

    @Mock
    private FileObject fileObject;

    @Mock
    private FileContent fileContent;

    @Mock
    private ErrorListener errorListener;

    @Spy
    private InputStream emptyInputStream = new ByteArrayInputStream("".getBytes());

    @Spy
    private InputStream oneLineInputStream = new ByteArrayInputStream("TEST".getBytes());

    private DefaultFileProcessor defaultFileProcessor;

    @Before
    public void setUp() throws FileSystemException {

        when(fileObject.getContent()).thenReturn(fileContent);

        defaultFileProcessor = new DefaultFileProcessor(lineProcessor, 1);
    }

    @Test
    public void emptyFileShouldNoTriggerLineProcessor() throws FileProcessingException, LineProcessingException, FileSystemException {

        when(fileContent.getInputStream()).thenReturn(emptyInputStream);

        defaultFileProcessor.process(fileObject);

        verify(lineProcessor, never()).process(any());
    }

    @Test
    public void singleLineShouldTriggerLinerProcessorOnce() throws FileSystemException, FileProcessingException, LineProcessingException {

        when(fileContent.getInputStream()).thenReturn(oneLineInputStream);

        defaultFileProcessor.process(fileObject);

        verify(lineProcessor).process(any());
    }

    @Test
    public void errorsShouldBeReportedToListeners() throws LineProcessingException, FileProcessingException, FileSystemException {

        when(fileContent.getInputStream()).thenReturn(oneLineInputStream);

        final Line line = new Line(1, "TEST");

        doThrow(new LineProcessingException(line, new RuntimeException()))
                .when(lineProcessor)
                .process(any());

        defaultFileProcessor.registerErrorListener(errorListener);

        defaultFileProcessor.process(fileObject);

        verify(errorListener).notifyException(any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroThreadsShouldBeRejected() {
        new DefaultFileProcessor(lineProcessor, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeNumberOfThreadsShouldBeRejected() {
        new DefaultFileProcessor(lineProcessor, -1);
    }

    @Test(expected = FileProcessingException.class)
    public void unreadableFileShouldThrowException() throws FileSystemException, FileProcessingException {

        when(fileContent.getInputStream()).thenThrow(new FileSystemException(""));

        defaultFileProcessor.process(fileObject);
    }
}
