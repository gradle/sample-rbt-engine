package org.gradle.rbt.engine;

import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.platform.engine.*;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;
import org.junit.platform.engine.support.discovery.EngineDiscoveryRequestResolver;
import org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.stream.Collectors;

public final class HierarchicalResourceBasedTestEngine extends HierarchicalTestEngine<EmptyContext> {
    public static final Logger LOGGER = LoggerFactory.getLogger(HierarchicalResourceBasedTestEngine.class);

    public static final String ENGINE_ID = "rbt-hierarchical-engine";
    public static final String ENGINE_NAME = "Hierarchical Resource Based Test Engine";

    // installs the JUL -> SLF4J bridge so JUL logs go through SLF4J/logback (which you configured to use System.out)
    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    @Override
    protected EmptyContext createExecutionContext(ExecutionRequest request) {
        return new EmptyContext();
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
                .addSelectorResolver(new HierarchicalResourceBasedSelectorResolver())
                .build()
                .resolve(discoveryRequest, engineDescriptor);

        return engineDescriptor;
    }
}
