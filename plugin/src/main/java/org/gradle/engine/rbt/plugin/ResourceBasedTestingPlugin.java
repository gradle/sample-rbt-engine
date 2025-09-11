package org.gradle.engine.rbt.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.DependencyScopeConfiguration;
import org.gradle.api.artifacts.ResolvableConfiguration;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.engine.rbt.plugin.task.ExecuteTestsTask;
import org.gradle.engine.rbt.plugin.task.ScanResourcesTask;
import org.gradle.rbt.engine.ResourceBasedTestEngine;

@SuppressWarnings("UnstableApiUsage")
public abstract class ResourceBasedTestingPlugin implements Plugin<Project> {
    public static final Logger LOGGER = Logging.getLogger(ScanResourcesTask.class);
    private static final String RBT_EXTENSION_NAME = "rbt";
    private static final String SCAN_TASK_NAME = "scanForTests";
    private static final String EXECUTE_TASK_NAME = "executeTests";
    private static final String RBT_TASKS_GROUP = "Resource Based Testing";
    private static final String DEFAULT_TEST_DEFINITIONS_DIR_PATH = "src/test/testDefinitions";
    private static final String DEFAULT_ENGINE_CLASS_FILE_PATH = "generated/tests";
    private static final String DEFAULT_JUNIT_PLATFORM_VERSION = "1.13.4";

    @Override
    public void apply(Project project) {
        TaskContainer tasks = project.getTasks();
        ExtensionContainer extensions = project.getExtensions();

        ResourceBasedTestingExtension extension = extensions.create(RBT_EXTENSION_NAME, ResourceBasedTestingExtension.class);

        TaskProvider<ScanResourcesTask> scanTask = tasks.register(SCAN_TASK_NAME, ScanResourcesTask.class, task -> {
            task.setGroup(RBT_TASKS_GROUP);
            task.setDescription("Scans resources for tests and creates a dummy class file to use as an input for the test engine (to support Gradle's reliance on class-based testing)");

            task.getInputDir().convention(project.getLayout().getProjectDirectory().dir(DEFAULT_TEST_DEFINITIONS_DIR_PATH));
            task.getResultsDir().convention(project.getLayout().getBuildDirectory().dir(DEFAULT_ENGINE_CLASS_FILE_PATH));
        });

        Provider<DependencyScopeConfiguration> executeTestsImplementation = project.getConfigurations().dependencyScope(EXECUTE_TASK_NAME + "Implementation");
        Provider<ResolvableConfiguration> executeTestsClasspath = project.getConfigurations().resolvable(EXECUTE_TASK_NAME + "Classpath", c -> {
            c.extendsFrom(executeTestsImplementation.get());
        });
        executeTestsImplementation.get().fromDependencyCollector(extension.getDependencies().getImplementation());

        TaskProvider<ExecuteTestsTask> executeTask = tasks.register(EXECUTE_TASK_NAME, ExecuteTestsTask.class, task -> {
            task.setGroup(RBT_TASKS_GROUP);
            task.setDescription("Executes tests using the resource-based testing engine");

            task.getJUnitPlatformVersion().convention(DEFAULT_JUNIT_PLATFORM_VERSION);
            task.getConfigurableTestClassesDirs().from(scanTask.flatMap(ScanResourcesTask::getResultsDir));
            task.setTestClassesDirs(task.getConfigurableTestClassesDirs());
            task.useJUnitPlatform(options -> {
                options.includeEngines(ResourceBasedTestEngine.ENGINE_ID);
                options.excludeEngines("junit-jupiter");
            });
            task.getFailOnNoDiscoveredTests().set(false);
            task.setClasspath(executeTestsClasspath.get());
            task.doFirst(t -> LOGGER.lifecycle("Executing (dummy) tests in: " + ((ExecuteTestsTask) t).getConfigurableTestClassesDirs().getAsPath()));
        });

        Provider<Dependency> platformDependencyProvider = executeTask.map(t -> project.getDependencies().create("org.junit.platform:junit-platform-launcher:" + t.getJUnitPlatformVersion().get()));
        extension.getDependencies().getImplementation().add(platformDependencyProvider);
        extension.getDependencies().getImplementation().add(ResourceBasedTestEngine.ENGINE_COORDINATES);
        extension.getDependencies().getImplementation().add(executeTask.map(ExecuteTestsTask::getConfigurableTestClassesDirs).get());
    }
}
