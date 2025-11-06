package org.gradle.rbt.engine;

import org.gradle.rbt.descriptor.ResourceBasedTestDescriptor;
import org.gradle.rbt.util.DirectoryScanner;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.discovery.DirectorySelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.FileSelector;
import org.junit.platform.engine.support.discovery.SelectorResolver;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ResourceBasedSelectorResolver implements SelectorResolver {
    public static final Logger LOGGER = LoggerFactory.getLogger(ResourceBasedTestEngine.class);

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
        if (directoryScanner.getTestFileParser().isValidTestDefinitionFile(file)) {
            LOGGER.info(() -> "Found test definitions in: " + file.getAbsolutePath());

            Set<Match> tests = directoryScanner.getTestFileParser().parseTestNames(file).stream()
                    .map(testName -> context.addToParent(parent -> Optional.of(new ResourceBasedTestDescriptor(parent.getUniqueId(), file, testName))))
                    .map(Optional::orElseThrow)
                    .map(Match::exact)
                    .collect(Collectors.toSet());
            return Resolution.matches(tests);
        } else {
            return Resolution.unresolved();
        }
    }
}
