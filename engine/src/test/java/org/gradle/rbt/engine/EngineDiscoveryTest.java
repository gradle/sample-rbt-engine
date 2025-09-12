package org.gradle.rbt.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.gradle.engine.test.TestDescriptorAssertions.assertDiscoveredTests;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectDirectory;

import org.assertj.core.util.Sets;
import org.gradle.engine.test.SampleTestClass;
import org.gradle.engine.test.TestResourcesUtil;
import org.gradle.rbt.util.Inputs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.platform.testkit.engine.EngineDiscoveryResults;
import org.junit.platform.testkit.engine.EngineTestKit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class EngineDiscoveryTest extends AbstractTestDefinitionsTest {
    @Test
    public void discoverySucceedsGivenNormalTestClassSelector() {
        EngineDiscoveryResults results = EngineTestKit.engine(ResourceBasedTestEngine.ENGINE_ID)
                .selectors(selectClass(SampleTestClass.class))
                .discover();

        assertUsingResourceBasedEngine(results);
        assertDiscoveredTests(results, Collections.emptyMap()); // But finds no tests
    }

    @Test
    public void discoverySucceedsGivenDirectorySelector() {
        EngineDiscoveryResults results = EngineTestKit.engine(ResourceBasedTestEngine.ENGINE_ID)
                .selectors(selectDirectory(testDefinitionsDir))
                .discover();

        assertUsingResourceBasedEngine(results);
        assertDiscoveredTests(
            results,
            Map.ofEntries(
                testDescriptor("tests.xml", "foo", "bar"),
                testDescriptor("sub/more-tests.xml", "baz")));
    }

    @Test
    public void discoverySucceedsGivenDummySentinelTestClassAndEmptyResourcesRoot() throws IOException {
        Path emptyDir = Files.createTempDirectory("nothing-here");
        System.setProperty(Inputs.TEST_RESOURCES_ROOT_DIR_PROP, emptyDir.toString());

        EngineDiscoveryResults results = EngineTestKit.engine(ResourceBasedTestEngine.ENGINE_ID)
                .selectors(selectClass(ResourceBasedTestEngine.ENGINE_DUMMY_CLASS_NAME))
                .discover();

        assertUsingResourceBasedEngine(results);
        assertDiscoveredTests(results, Collections.emptyMap()); // But finds no tests
    }

    @Test
    public void discoverySucceedsGivenDummySentinelTestClassAndProperResourcesRoot() {
        System.setProperty(Inputs.TEST_RESOURCES_ROOT_DIR_PROP, testDefinitionsDir.getAbsolutePath());

        EngineDiscoveryResults results = EngineTestKit.engine(ResourceBasedTestEngine.ENGINE_ID)
                .selectors(selectClass(ResourceBasedTestEngine.ENGINE_DUMMY_CLASS_NAME))
                .discover();

        assertUsingResourceBasedEngine(results);
        assertDiscoveredTests(
            results,
            Map.ofEntries(
                testDescriptor("tests.xml", "foo", "bar"),
                testDescriptor("sub/more-tests.xml", "baz")));
    }

    private static void assertUsingResourceBasedEngine(EngineDiscoveryResults results) {
        assertEquals(ResourceBasedTestEngine.ENGINE_ID, results.getEngineDescriptor().getUniqueId().getEngineId().orElseThrow());
        assertEquals(ResourceBasedTestEngine.ENGINE_NAME, results.getEngineDescriptor().getDisplayName());
    }
}
