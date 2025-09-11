package org.gradle.engine.rbt.plugin.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.tasks.*;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class ScanResourcesTask extends DefaultTask {
    public static final Logger LOGGER = Logging.getLogger(ScanResourcesTask.class);
    public static final String ENGINE_CLASS_NAME = "EngineEntryPoint";

    /** Input directory containing resource files. */
    @InputDirectory
    @PathSensitive(PathSensitivity.RELATIVE)
    public abstract DirectoryProperty getInputDir();

    /** Output directory for dummy test classes built from test resources. */
    @OutputDirectory
    public abstract DirectoryProperty getResultsDir();

    @TaskAction
    public void scan() {
        File inputDir = getInputDir().getAsFile().get();
        LOGGER.lifecycle("Scanning resources in: " + inputDir.getAbsolutePath());
        List<File> foundFiles = scanDirectory(inputDir);
        for (File file : foundFiles) {
            LOGGER.lifecycle("Found test resource file: " + file.getAbsolutePath());
        }

        File outputDir = getResultsDir().getAsFile().get();
        File outputFile = generateEngineClass(outputDir);
        LOGGER.lifecycle("Generated engine class at: " + outputFile.getAbsolutePath());
    }

    private List<File> scanDirectory(File dir) {
        List<File> filesFound = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    filesFound.addAll(scanDirectory(file));
                } else {
                    filesFound.add(file);
                }
            }
        }
        return filesFound;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File generateEngineClass(File location) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classWriter.visit(
                Opcodes.V1_8,
                Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER,
                ENGINE_CLASS_NAME,
                null,
                "java/lang/Object",
                null
        );
        classWriter.visitEnd();

        byte[] classBytes = classWriter.toByteArray();
        location.mkdirs();

        File result = new File(location, ENGINE_CLASS_NAME + ".class");
        try (FileOutputStream output = new FileOutputStream(result)) {
            output.write(classBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
