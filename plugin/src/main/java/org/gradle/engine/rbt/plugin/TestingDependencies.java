package org.gradle.engine.rbt.plugin;

import org.gradle.api.artifacts.dsl.Dependencies;
import org.gradle.api.artifacts.dsl.DependencyCollector;

public interface TestingDependencies extends Dependencies {
    DependencyCollector getImplementation();
}
