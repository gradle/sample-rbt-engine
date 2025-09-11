package org.gradle.rbt.engine;

import org.junit.platform.engine.*;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;
import org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine;

public final class ResourceBasedTestEngine extends HierarchicalTestEngine<ResourceBasedExecutionContext> {
    public static final String ENGINE_ID = "rbt-engine";
    public static final String ENGINE_NAME = "Resource Based Test Engine";
    public static final String ENGINE_COORDINATES = "org.gradle:engine:0.1.0"; // These need to be kept in sync with the group + name in the build.gradle file

    @Override
    public String getId() {
        return ENGINE_ID;
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId uniqueId) {
        EngineDescriptor engineDescriptor = new EngineDescriptor(uniqueId, ENGINE_NAME);

        return engineDescriptor;
    }

    @Override
    public ResourceBasedExecutionContext createExecutionContext(ExecutionRequest request) {
        return null;
    }
}
