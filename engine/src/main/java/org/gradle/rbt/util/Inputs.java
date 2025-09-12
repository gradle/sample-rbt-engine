package org.gradle.rbt.util;

import java.io.File;

public interface Inputs {
    String PROPERTY_PREFIX = "org.gradle.rbt.";

    String TEST_RESOURCES_ROOT_DIR_PROP = PROPERTY_PREFIX + "testResourcesRootDir";

    static File getTestResourcesRootDir() {
        String testResourcesRootDir = System.getProperty(TEST_RESOURCES_ROOT_DIR_PROP);
        return new File(testResourcesRootDir);
    }
}
