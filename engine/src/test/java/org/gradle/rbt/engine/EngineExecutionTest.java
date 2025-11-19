package org.gradle.rbt.engine;

import org.gradle.engine.test.SampleTestClass;
import org.junit.jupiter.api.Test;
import org.junit.platform.testkit.engine.*;

import java.io.File;
import java.util.*;

import static org.gradle.engine.test.TestDescriptorAssertions.assertExecutedTests;
import static org.junit.platform.engine.discovery.DiscoverySelectors.*;

public final class EngineExecutionTest extends AbstractTestDefinitionsTest{
    @Test
    public void executionSucceedsGivenNormalTestClass() {
        EngineExecutionResults results = EngineTestKit.engine(ResourceBasedTestEngine.ENGINE_ID)
                .selectors(selectClass(SampleTestClass.class))
                .execute();

        // But executes no tests
        assertExecutedTests(results, Collections.emptyMap());
    }

    @Test
    public void executionSucceedsGivenDirectorySelector() {
        EngineExecutionResults results = EngineTestKit.engine(ResourceBasedTestEngine.ENGINE_ID)
                .selectors(selectDirectory(testDefinitionsDir.getAbsolutePath()))
                .execute();

        assertExecutedTests(
            results,
            Map.ofEntries(
                testDescriptor("tests.xml", "foo", "bar"),
                testDescriptor("sub/more-tests.xml", "baz")));
    }

    @Test
    public void executionSucceedsGivenFileSelector() {
        EngineExecutionResults results = EngineTestKit.engine(ResourceBasedTestEngine.ENGINE_ID)
                .selectors(selectFile(new File(testDefinitionsDir, "tests.xml").getAbsolutePath()))
                .execute();

        assertExecutedTests(
                results,
                Map.ofEntries(
                        testDescriptor("tests.xml", "foo", "bar")));
    }
}
