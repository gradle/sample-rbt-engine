package org.gradle.rbt.engine;

import org.gradle.rbt.descriptor.ResourceBasedTestDescriptor;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.platform.engine.*;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;
import org.junit.platform.engine.support.discovery.EngineDiscoveryRequestResolver;

import java.util.stream.Collectors;

public final class ResourceBasedTestEngine implements TestEngine {
    public static final Logger LOGGER = LoggerFactory.getLogger(ResourceBasedTestEngine.class);

    public static final String ENGINE_ID = "rbt-engine";
    public static final String ENGINE_NAME = "Resource Based Test Engine";
    public static final String ENGINE_COORDINATES = "org.gradle:engine:0.1.0"; // These need to be kept in sync with the group + name in the build.gradle file

    public static final String ENGINE_DUMMY_CLASS_NAME = "EngineEntryPoint";

    @Override
    public String getId() {
        return ENGINE_ID;
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId uniqueId) {
        LOGGER.info(() -> {
            String selectorsMsg = discoveryRequest.getSelectorsByType(DiscoverySelector.class).stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n", "\t", ""));
            return "Discovering tests with engine: " + uniqueId + " using selectors:\n" + selectorsMsg;
        });

        EngineDescriptor engineDescriptor = new EngineDescriptor(uniqueId, ENGINE_NAME);

        EngineDiscoveryRequestResolver.builder()
                .addSelectorResolver(new ResourceBasedSelectorResolver())
                .build()
                .resolve(discoveryRequest, engineDescriptor);

        return engineDescriptor;
    }

    @Override
    public void execute(ExecutionRequest executionRequest) {
        LOGGER.info(() -> "Executing tests with engine: " + executionRequest.getRootTestDescriptor().getUniqueId());

        EngineExecutionListener listener = executionRequest.getEngineExecutionListener();
        executionRequest.getRootTestDescriptor().getChildren().forEach(test -> {
            if (test instanceof ResourceBasedTestDescriptor) {
                listener.executionStarted(test);
                LOGGER.info(() -> "Executing test: " + test);
                listener.executionFinished(test, TestExecutionResult.successful());
            } else {
                throw new IllegalStateException("Cannot execute test: " + test + " of type: " + test.getClass().getName());
            }
        });
    }
}
