package org.gradle.engine;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectDirectory;

import org.gradle.engine.test.TestUtil;
import org.gradle.rbt.engine.ResourceBasedTestEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.platform.testkit.engine.EngineDiscoveryResults;
import org.junit.platform.testkit.engine.EngineTestKit;

import java.io.File;
import java.nio.file.Path;

public final class EngineDiscoveryTest {
    @TempDir
    Path tempDir;
    File testDefinitionsDir;

    @BeforeEach
    public void setup() {
        testDefinitionsDir = new File(tempDir.toFile(), "testDefinitions");
        TestUtil.copyResourceDir("testDefinitions", testDefinitionsDir);
    }

    @Test
    public void verifyDiscovery() {
        EngineDiscoveryResults results = EngineTestKit.engine(ResourceBasedTestEngine.ENGINE_ID)
                .selectors(selectDirectory(testDefinitionsDir))
                .discover();

        assertEquals(ResourceBasedTestEngine.ENGINE_ID, results.getEngineDescriptor().getUniqueId().getEngineId().get());
        assertEquals(ResourceBasedTestEngine.ENGINE_NAME, results.getEngineDescriptor().getDisplayName());

        assertEquals(emptyList(), results.getDiscoveryIssues());
    }
}
