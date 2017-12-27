package io.github.xstefanox.demo.filestreaming.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.vfs2.FileSelectInfo;
import org.apache.commons.vfs2.FileSelector;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

public class CombinedFileSelector implements FileSelector {

    private List<FileSelector> fileSelectors = new ArrayList<>();

    public CombinedFileSelector(final FileSelector fileSelectors, final FileSelector... others) {
        requireNonNull(fileSelectors, "fileSelectors must not be null");
        this.fileSelectors.add(fileSelectors);
        this.fileSelectors.addAll(asList(others));
    }

    @Override
    public boolean includeFile(final FileSelectInfo fileInfo) {

        return fileSelectors.stream()
                .map(fileSelector -> {
                    try {
                        return fileSelector.includeFile(fileInfo);
                    } catch (Exception e) {
                        throw new CombinedFileSelectorException(fileInfo, fileSelector, e);
                    }
                })
                .reduce((result, cur) -> result && cur)
                .orElse(false);
    }

    @Override
    public boolean traverseDescendents(FileSelectInfo fileInfo) {
        return fileSelectors.stream()
                .map(fileSelector -> {
                    try {
                        return fileSelector.traverseDescendents(fileInfo);
                    } catch (Exception e) {
                        throw new CombinedFileSelectorException(fileInfo, fileSelector, e);
                    }
                })
                .reduce((result, cur) -> result && cur)
                .orElse(false);
    }
}
