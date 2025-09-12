package org.gradle.engine.rbt.plugin.task;

import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.*;
import org.gradle.api.tasks.testing.Test;

public abstract class ExecuteTestsTask extends Test {
    /** Input directory containing resource files. */
    @InputDirectory
    @PathSensitive(PathSensitivity.RELATIVE)
    public abstract DirectoryProperty getInputDir();

    @Input
    public abstract Property<String> getJUnitPlatformVersion();

    /** Dummy test classes generated from resource files. */
    @InputFiles
    public abstract ConfigurableFileCollection getConfigurableTestClassesDirs();

    // jvmArgs exists on the Test task, not the AbstractTestTask
    /**
     * Sets a test engine parameter as a JVM argument.
     *
     * @param key The parameter key.
     * @param value The parameter value.
     */
    public void setTestEngineParam(String key, String value) {
        jvmArgs("-D" + key + "=" + value);
    }
}
