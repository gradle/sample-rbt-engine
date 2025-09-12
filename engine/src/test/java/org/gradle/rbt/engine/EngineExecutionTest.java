package org.gradle.rbt.engine;

import org.gradle.engine.test.SampleTestClass;
import org.gradle.rbt.util.Inputs;
import org.junit.jupiter.api.Test;
import org.junit.platform.testkit.engine.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.gradle.engine.test.TestDescriptorAssertions.assertExecutedTests;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

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
        System.setProperty(Inputs.TEST_RESOURCES_ROOT_DIR_PROP, testDefinitionsDir.getAbsolutePath());

        EngineExecutionResults results = EngineTestKit.engine(ResourceBasedTestEngine.ENGINE_ID)
                .selectors(selectClass(ResourceBasedTestEngine.ENGINE_DUMMY_CLASS_NAME))
                .execute();

        assertExecutedTests(
            results,
            Map.ofEntries(
                testDescriptor("tests.xml", "foo", "bar"),
                testDescriptor("sub/more-tests.xml", "baz")));
    }

    @Test
    public void executionSucceedsGivenDummySentinelTestClassAndEmptyResourcesRoot() throws IOException {
        Path emptyDir = Files.createTempDirectory("nothing-here");
        System.setProperty(Inputs.TEST_RESOURCES_ROOT_DIR_PROP, emptyDir.toString());

        EngineExecutionResults results = EngineTestKit.engine(ResourceBasedTestEngine.ENGINE_ID)
                .selectors(selectClass(ResourceBasedTestEngine.ENGINE_DUMMY_CLASS_NAME))
                .execute();

        // But executes no tests
        assertExecutedTests(results, Collections.emptyMap());
    }

    @Test
    public void executionSucceedsGivenDummySentinelTestClassAndProperResourcesRoot() {
        System.setProperty(Inputs.TEST_RESOURCES_ROOT_DIR_PROP, testDefinitionsDir.getAbsolutePath());

        EngineExecutionResults results = EngineTestKit.engine(ResourceBasedTestEngine.ENGINE_ID)
                .selectors(selectClass(ResourceBasedTestEngine.ENGINE_DUMMY_CLASS_NAME))
                .execute();

        assertExecutedTests(
            results,
            Map.ofEntries(
                testDescriptor("tests.xml", "foo", "bar"),
                testDescriptor("sub/more-tests.xml", "baz")));
    }
}
