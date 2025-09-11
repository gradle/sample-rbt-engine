package org.gradle.engine.rbt.plugin.task;

import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.testing.Test;

public abstract class ExecuteTestsTask extends Test {
    public static final Logger LOGGER = Logging.getLogger(ScanResourcesTask.class);

    @Input
    public abstract Property<String> getJUnitPlatformVersion();

    @InputFiles
    public abstract ConfigurableFileCollection getConfigurableTestClassesDirs();

    // jvmArgs exists on the Test task, not the AbstractTestTask
    /**
     * Sets a test engine parameter as a JVM argument.
     *
     * @param key The parameter key.
     * @param value The parameter value.
     */
    private void setTestEngineParam(String key, String value) {
        jvmArgs("-DtestEngineInput." + key + "=" + value);
    }
}
