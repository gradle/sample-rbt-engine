package org.gradle.rbt.engine;

import org.gradle.rbt.descriptor.ResourceBasedTestDescriptor;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.platform.engine.*;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;
import org.junit.platform.engine.support.discovery.EngineDiscoveryRequestResolver;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.stream.Collectors;

public final class ResourceBasedTestEngine implements TestEngine {
    public static final Logger LOGGER = LoggerFactory.getLogger(ResourceBasedTestEngine.class);

    public static final String ENGINE_ID = "rbt-engine";
    public static final String ENGINE_NAME = "Resource Based Test Engine";

    // installs the JUL -> SLF4J bridge so JUL logs go through SLF4J/logback (which you configured to use System.out)
    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    @Override
    public String getId() {
        return ENGINE_ID;
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId uniqueId) {
        LOGGER.info(() -> {
            String selectorsMsg = discoveryRequest.getSelectorsByType(DiscoverySelector.class).stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n\t", "\t", ""));
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
                String worker = System.getProperty("org.gradle.test.worker");
                LOGGER.info(() -> "Worker: " + worker + " executing test: " + test);
                listener.executionFinished(test, TestExecutionResult.successful());
            } else {
                throw new IllegalStateException("Cannot execute test: " + test + " of type: " + test.getClass().getName());
            }
        });
    }
}
