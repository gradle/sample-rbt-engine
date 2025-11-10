package org.example;

import java.io.File;
import java.util.Set;

import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.events.OperationType;
import org.gradle.tooling.events.test.*;

@SuppressWarnings("UnstableApiUsage")
public class Main {
    public static void main(String[] args) {
        GradleConnector connector = GradleConnector.newConnector();

        connector.forProjectDirectory(new File("../demo-m1"));

        try (ProjectConnection connection = connector.connect()) {
            // Configure the launcher to listen and print test metadata events
            BuildLauncher launcher = connection.newBuild();
            launcher.addProgressListener(progressEvent -> {
                if (progressEvent instanceof TestMetadataEvent testMetadataEvent) {
                    System.out.println("Received test metadata event: " + testMetadataEvent.getDisplayName());
                    testMetadataEvent.getValues().forEach((key, value) -> System.out.println("  " + key + " = " + value));
                }
            }, Set.of(OperationType.TEST, OperationType.TEST_METADATA));

            // Run the demo-m1 build, rerunning tests to ensure they aren't found to be up-to-date
            launcher.forTasks("test", "--rerun");
            launcher.setStandardOutput(System.out);
            launcher.setStandardError(System.err);
            launcher.run();
        }
    }
}
