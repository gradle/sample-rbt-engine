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
        connector.forProjectDirectory(new File("../demo-m2-3"));

        try (ProjectConnection connection = connector.connect()) {
            // Configure the launcher to listen and print test metadata events
            BuildLauncher launcher = connection.newBuild();
            System.out.println();
            launcher.addProgressListener(progressEvent -> {
                if (progressEvent instanceof TestMetadataEvent testMetadataEvent) {
                    System.out.println("Received test metadata event for " + testMetadataEvent.getDescriptor().getParent().getDisplayName());
                    if (testMetadataEvent instanceof TestKeyValueMetadataEvent keyValues) {
                        System.out.printf("Key-values (size: %d):%n", keyValues.getValues().size());
                        keyValues.getValues().forEach((key, value) -> System.out.println("\t" + key + " = " + value));
                    } else if (testMetadataEvent instanceof TestFileAttachmentMetadataEvent fileAttachment) {
                        System.out.printf("File attachment (%s):%n\t%s%n", fileAttachment.getMediaType(), fileAttachment.getFile());
                    } else {
                        System.out.println("Unrecognized metadata event type: " + testMetadataEvent);
                    }
                    System.out.println();
                }
            }, Set.of(OperationType.TEST, OperationType.TEST_METADATA));

            System.out.println();

            // Run the demo-m2-3 build, rerunning tests to ensure they aren't found to be up-to-date
            launcher.forTasks("test", "--rerun");
            launcher.setStandardOutput(System.out);
            launcher.setStandardError(System.err);
            launcher.run();
        }
    }
}
