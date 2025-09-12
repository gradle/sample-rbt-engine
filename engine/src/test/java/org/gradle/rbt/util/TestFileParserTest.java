package org.gradle.rbt.util;

import org.gradle.engine.test.TestResourcesUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TestFileParserTest {
    @TempDir
    private Path tempDir;
    private File testDefinitionsDir;

    @BeforeEach
    public void setup() {
        testDefinitionsDir = new File(tempDir.toFile(), "testDefinitions");
        TestResourcesUtil.copyResourceDir("testDefinitions", testDefinitionsDir);
    }

    private final TestFileParser parser = new TestFileParser();

    @Test
    public void canParseValidFile() {
        File testDefinitions = new File(testDefinitionsDir, "tests.xml");
        List<String> testNames = parser.parseTestNames(testDefinitions);

        assertEquals(List.of("foo", "bar"), testNames);
    }

    @Test
    public void canParseInvalidFile() {
        File testDefinitions = new File(testDefinitionsDir, "invalid-file.not-a-test");
        List<String> testNames = parser.parseTestNames(testDefinitions);

        assertEquals(List.of(), testNames);
    }
}
