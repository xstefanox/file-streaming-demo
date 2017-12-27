package io.github.xstefanox.demo.filestreaming.service;

import io.github.xstefanox.demo.filestreaming.exception.FileProcessingException;
import io.github.xstefanox.demo.filestreaming.exception.InputDirectoryNotFoundException;
import io.github.xstefanox.demo.filestreaming.exception.InputNotDirectoryException;
import io.github.xstefanox.demo.filestreaming.exception.UnreadableInputDirectory;
import io.github.xstefanox.demo.filestreaming.listener.FileWriterErrorListener;
import io.github.xstefanox.demo.filestreaming.processor.FileProcessor;
import io.github.xstefanox.demo.filestreaming.processor.FileProcessorBuilder;
import io.github.xstefanox.demo.filestreaming.util.CombinedFileSelector;
import java.net.URI;
import java.util.List;
import org.apache.commons.vfs2.FileDepthSelector;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSelector;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileTypeSelector;
import org.apache.commons.vfs2.cache.NullFilesCache;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Arrays.asList;
import static org.apache.commons.vfs2.FileType.FILE;

public class DirectoryScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DirectoryScanner.class);
    private static final FileSelector FILES_ONLY = new CombinedFileSelector(new FileTypeSelector(FILE), new FileDepthSelector(1));
    private static final String ERRORS_SUFFIX = ".ERRORS";
    private static final String COMPLETED_DIRECTORY = "COMPLETED";

    private final FileSystemManager fileSystemManager;

    public DirectoryScanner(final FileSystemManager fileSystemManager) {
        this.fileSystemManager = fileSystemManager;
    }

    public void processDataInDirectory(final URI uri, final Integer nThreads) {

        final List<FileObject> files;

        final FileProcessorBuilder fileProcessorBuilder = FileProcessor.builder()
                .processLinesWith(line -> {
                    // do nothing
                })
                .usingThreads(nThreads);

        try (final FileObject baseDirectory = fileSystemManager.resolveFile(uri);
             final FileObject completedDirectory = baseDirectory.resolveFile(COMPLETED_DIRECTORY)) {

            if (!baseDirectory.exists()) {
                throw new InputDirectoryNotFoundException(baseDirectory);
            }

            if (!baseDirectory.isFolder()) {
                throw new InputNotDirectoryException(baseDirectory);
            }

            if (!baseDirectory.isReadable()) {
                throw new UnreadableInputDirectory(baseDirectory);
            }

            if (!completedDirectory.exists()) {
                LOGGER.debug("directory {} not found, creating", completedDirectory);
                completedDirectory.createFolder();
            }

            files = asList(baseDirectory.findFiles(FILES_ONLY));

        } catch (final FileSystemException e) {
            LOGGER.error("error processing directory {} : {}", uri, e.getMessage());
            return;
        }

        if (files.isEmpty()) {
            LOGGER.info("no file found in directory {}", uri);
            return;
        }

        LOGGER.info("found {} files in directory {}", files.size(), uri);

        for (final FileObject fileObject : files) {

            try (final FileObject completedDirectory = fileObject.getParent().resolveFile(COMPLETED_DIRECTORY);
                 final FileObject completedFile = completedDirectory.resolveFile(fileObject.getName().getBaseName());
                 final FileObject completedErrorsFile = completedDirectory.resolveFile(fileObject.getName().getBaseName() + ERRORS_SUFFIX);
                 final FileWriterErrorListener fileWriterErrorListener = new FileWriterErrorListener(completedErrorsFile)) {

                fileProcessorBuilder.writingErrorsUsing(fileWriterErrorListener)
                        .build()
                        .process(fileObject);

                fileObject.moveTo(completedFile);

            } catch (final FileProcessingException | FileSystemException e) {
                LOGGER.error("error processing file {} : {}", fileObject, e.getMessage());
            }
        }
    }

    public static void main(String... args) throws FileSystemException {

        final StandardFileSystemManager fileSystemManager = new StandardFileSystemManager();
        fileSystemManager.setFilesCache(new NullFilesCache());
        fileSystemManager.init();

        DirectoryScanner directoryScanner = new DirectoryScanner(fileSystemManager);
        directoryScanner.processDataInDirectory(URI.create("/tmp/test"), 5);
    }
}
