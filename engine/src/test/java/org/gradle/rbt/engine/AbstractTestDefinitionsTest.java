package org.gradle.rbt.engine;

import org.assertj.core.util.Sets;
import org.gradle.engine.test.TestResourcesUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

public abstract class AbstractTestDefinitionsTest {
    @TempDir
    protected Path tempDir;
    protected File testDefinitionsDir;

    @BeforeEach
    protected void setup() {
        testDefinitionsDir = new File(tempDir.toFile(), "testDefinitions");
        TestResourcesUtil.copyResourceDir("testDefinitions", testDefinitionsDir);
    }

    protected Map.Entry<Path, Set<String>> testDescriptor(String filePath, String... testNames) {
        File file = new File(testDefinitionsDir, filePath);
        Set<String> names = Sets.newLinkedHashSet(testNames);
        try {
            return Map.entry(file.toPath().toRealPath(), names);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
