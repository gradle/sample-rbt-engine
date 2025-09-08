package org.gradle.engine;

import org.junit.jupiter.engine.config.DefaultJupiterConfiguration;
import org.junit.jupiter.engine.config.JupiterConfiguration;
import org.junit.jupiter.engine.descriptor.ClassTestDescriptor;
import org.junit.platform.engine.*;

public final class ResourceBasedTestEngine implements TestEngine {
    public static final String ENGINE_ID = "rbt-engine";

    @Override
    public String getId() {
        return ENGINE_ID;
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId uniqueId) {
        Class<?> testClass;
        try {
            testClass = Class.forName("org.gradle.engine.test.SampleTests");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        JupiterConfiguration configuration = new DefaultJupiterConfiguration(discoveryRequest.getConfigurationParameters(), discoveryRequest.getOutputDirectoryProvider());

        return new ClassTestDescriptor(UniqueId.forEngine(ENGINE_ID), testClass, configuration);
    }

    @Override
    public void execute(ExecutionRequest request) {

    }
}
