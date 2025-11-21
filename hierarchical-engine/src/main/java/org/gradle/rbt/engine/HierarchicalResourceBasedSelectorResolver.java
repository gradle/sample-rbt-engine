package org.gradle.rbt.engine;

import org.gradle.rbt.descriptor.ResourceBasedFileDescriptor;
import org.gradle.rbt.util.DirectoryScanner;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.discovery.DirectorySelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.FileSelector;
import org.junit.platform.engine.support.discovery.SelectorResolver;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class HierarchicalResourceBasedSelectorResolver implements SelectorResolver {
    public static final Logger LOGGER = LoggerFactory.getLogger(HierarchicalResourceBasedSelectorResolver.class);

    private final DirectoryScanner directoryScanner = new DirectoryScanner();

    @Override
    public Resolution resolve(DirectorySelector selector, Context context) {
        List<File> contents = directoryScanner.scanDirectory(selector.getDirectory(), true)
                .stream()
                .toList();

        if (!contents.isEmpty()) {
            Set<DiscoverySelector> selectors = new LinkedHashSet<>();
            contents.stream()
                    .filter(File::isFile)
                    .forEach(file -> selectors.add(DiscoverySelectors.selectFile(file.getAbsolutePath())));
            contents.stream()
                    .filter(File::isDirectory)
                    .forEach(file -> selectors.add(DiscoverySelectors.selectDirectory(file.getAbsolutePath())));

            return Resolution.selectors(selectors);
        } else {
            return Resolution.unresolved();
        }
    }

    @Override
    public Resolution resolve(FileSelector selector, Context context) {
        File file = selector.getFile();
        LOGGER.info(() -> "Found test definitions in: " + file.getAbsolutePath());
        TestDescriptor fileDescriptor = context.addToParent(parent -> Optional.of(new ResourceBasedFileDescriptor(parent.getUniqueId(), file))).orElseThrow();
        return Resolution.match(Match.exact(fileDescriptor));
    }
}
