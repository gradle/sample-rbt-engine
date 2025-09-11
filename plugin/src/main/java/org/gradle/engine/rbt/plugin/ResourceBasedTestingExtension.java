package org.gradle.engine.rbt.plugin;

import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Nested;

public interface ResourceBasedTestingExtension {
    Property<String> getJUnitPlatformVersion();

    @Nested
    TestingDependencies getDependencies();
}
