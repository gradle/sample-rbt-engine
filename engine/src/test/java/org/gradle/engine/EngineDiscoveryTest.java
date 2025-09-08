package org.gradle.engine;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import org.junit.jupiter.api.Test;
import org.junit.platform.testkit.engine.EngineDiscoveryResults;
import org.junit.platform.testkit.engine.EngineTestKit;

import org.gradle.engine.test.SampleTests;

public final class EngineDiscoveryTest {
    @Test
    public void verifyJupiterDiscovery() {
        EngineDiscoveryResults results = EngineTestKit.engine(ResourceBasedTestEngine.ENGINE_ID)
                .selectors(selectClass(SampleTests.class))
                .discover();

        assertEquals(ResourceBasedTestEngine.ENGINE_ID, results.getEngineDescriptor().getUniqueId().getEngineId().get());
        assertEquals(emptyList(), results.getDiscoveryIssues());

        // TODO: remove this hardcoded test class after actually implementing test discovery
        assertEquals("SampleTests", results.getEngineDescriptor().getDisplayName());
    }
}
