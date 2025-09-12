package org.gradle.engine.test;

import org.gradle.rbt.descriptor.ResourceBasedTestDescriptor;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.testkit.engine.EngineDiscoveryResults;
import org.junit.platform.testkit.engine.EngineExecutionResults;
import org.junit.platform.testkit.engine.Event;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TestDescriptorAssertions {
    private TestDescriptorAssertions() {
        throw new IllegalStateException("Utility class");
    }

    public static void assertExecutedTests(EngineExecutionResults results, Map<Path, Set<String>> expectedTests) {
        Map<Path, Set<String>> actualTests = new HashMap<>();
        results.testEvents().list().stream()
                .map(Event::getTestDescriptor)
                .forEach(testDescriptor -> loadFromDescriptor((ResourceBasedTestDescriptor) testDescriptor, actualTests));

        assertThat(actualTests).isEqualTo(expectedTests);
    }

    public static void assertDiscoveredTests(EngineDiscoveryResults results, Map<Path, Set<String>> expectedTests) {
        assertEquals(emptyList(), results.getDiscoveryIssues());

        Set<? extends TestDescriptor> discoveredTestDescriptors = results.getEngineDescriptor().getChildren();
        assertTrue(discoveredTestDescriptors.stream().allMatch(td -> td instanceof ResourceBasedTestDescriptor));

        Map<Path, Set<String>> actualTests = new HashMap<>();
        discoveredTestDescriptors.forEach(testDescriptor -> loadFromDescriptor((ResourceBasedTestDescriptor) testDescriptor, actualTests));

        assertThat(actualTests).isEqualTo(expectedTests);
    }

    private static void loadFromDescriptor(ResourceBasedTestDescriptor testDescriptor, Map<Path, Set<String>> actualTests) {
        try {
            actualTests.computeIfAbsent(testDescriptor.getFile().toPath().toRealPath(), f -> new HashSet<>()).add(testDescriptor.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
