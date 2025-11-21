package org.gradle.rbt.engine;

import org.gradle.rbt.descriptor.ResourceBasedTestDescriptor;
import org.junit.platform.engine.*;
import org.junit.platform.engine.reporting.FileEntry;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;
import org.junit.platform.engine.support.discovery.EngineDiscoveryRequestResolver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Locale;

public final class ResourceBasedTestEngine implements TestEngine {

    public static final String ENGINE_ID = "rbt-engine";
    public static final String ENGINE_NAME = "Resource Based Test Engine";

    @Override
    public String getId() {
        return ENGINE_ID;
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId uniqueId) {
        EngineDescriptor engineDescriptor = new EngineDescriptor(uniqueId, ENGINE_NAME);

        EngineDiscoveryRequestResolver.builder()
                .addSelectorResolver(new ResourceBasedSelectorResolver())
                .build()
                .resolve(discoveryRequest, engineDescriptor);

        return engineDescriptor;
    }

    @Override
    public void execute(ExecutionRequest executionRequest) {
        EngineExecutionListener listener = executionRequest.getEngineExecutionListener();

        for (TestDescriptor test : executionRequest.getRootTestDescriptor().getChildren()) {
            if (!(test instanceof ResourceBasedTestDescriptor)) {
                throw new IllegalStateException("Cannot execute test: " + test + " of type: " + test.getClass().getName());
            }
            listener.executionStarted(test);
            if (test.toString().toLowerCase(Locale.ROOT).contains("fail")) {
                System.out.println("Test engine fails tests with names containing the 'fail' substring " + test);
                listener.executionFinished(test, TestExecutionResult.failed(new RuntimeException("Test failed because its name contains the 'fail' substring")));
            } else if (test.toString().toLowerCase(Locale.ROOT).contains("skip")) {
                System.out.println("Test engine skips tests with names containing the 'skip' substring " + test);
                listener.executionSkipped(test, "Test was ignored by the test engine");
            } else if (test.toString().toLowerCase(Locale.ROOT).contains("publishmetadata")) {
                System.out.println("Test engine publishes some metadata for tests with names containing the 'publishMetadata' substring " + test);
                listener.reportingEntryPublished(test, ReportEntry.from("non-class-based-testing", "check"));
                listener.executionFinished(test, TestExecutionResult.successful());
            } else if (test.toString().toLowerCase(Locale.ROOT).contains("publishfile")) {
                System.out.println("Test engine publishes a file entry for tests with names containing the 'publishFile' substring " + test);
                File file = new File(System.getProperty("java.io.tmpdir"), "tests.txt");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                    writer.write("Some info about " + executionRequest.getRootTestDescriptor() + System.lineSeparator());
                } catch (Exception e) {
                    throw new RuntimeException("Could not write report entry: " + file, e);
                }
                listener.fileEntryPublished(test, FileEntry.from(file.toPath(), "text/plain"));
                listener.executionFinished(test, TestExecutionResult.successful());
            } else {
                // pass the rest of the tests
                listener.executionFinished(test, TestExecutionResult.successful());
            }
        }
    }
}
