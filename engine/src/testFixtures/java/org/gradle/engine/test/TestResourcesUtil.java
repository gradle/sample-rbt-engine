package org.gradle.engine.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public abstract class TestResourcesUtil {
    private TestResourcesUtil() {
        throw new IllegalStateException("Can't instantiate utility class!");
    }

    /**
     * Reads a text resource file from the classpath and returns its content as a String.
     * <p>
     * The resource file should be present directly on the classpath, <strong>NOT</strong> inside a JAR file on the classpath.
     *
     * @param resourceFileRelativePath the relative path to the resource file (e.g., 'sample1/sample1.txt') from the classpath root
     * @return the content of the resource file as a String
     */
    public static String readResourceFile(String resourceFileRelativePath) {
        var resource = TestResourcesUtil.class.getClassLoader().getResource(resourceFileRelativePath);
        if (resource == null) {
            throw new IllegalArgumentException("Resource file '" + resourceFileRelativePath + "' not found.");
        }

        try {
            return Files.readString(new File(resource.toURI()).toPath());
        } catch (Exception e) {
            throw new RuntimeException("Failed to read resource file: " + resource, e);
        }
    }

    /**
     * Copies a resource directory from the classpath into the given destination directory.
     * <p>
     * The directory can be present directly on the classpath, or inside a JAR file on the classpath.
     *
     * @param resourceDirName the name of the directory in src/test/resources to copy (e.g., 'sample1')
     * @param destDir the destination directory (e.g., tempDir)
     */
    public static void copyResourceDir(String resourceDirName, File destDir) {
        var resource = TestResourcesUtil.class.getClassLoader().getResource(resourceDirName);
        if (resource == null) {
            throw new IllegalArgumentException("Resource directory '" + resourceDirName + "' not found.");
        }

        try {
            if ("jar".equals(resource.getProtocol())) {
                extractFromJar(resourceDirName, destDir, resource);
            } else {
                File resourceFile = new File(resource.toURI());
                copyDirectory(resourceFile, destDir);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to copy resource directory: " + resource, e);
        }
    }

    private static void extractFromJar(String resourceDirName, File destDir, URL resource) throws IOException {
        var jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
        try (var jarFile = new java.util.jar.JarFile(new File(jarPath))) {
            var entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                var entry = entries.nextElement();
                if (entry.getName().startsWith(resourceDirName) && !entry.isDirectory()) {
                    var inputStream = jarFile.getInputStream(entry);
                    var destFile = new File(destDir, entry.getName().substring(resourceDirName.length() + 1));
                    destFile.getParentFile().mkdirs();
                    Files.copy(inputStream, destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    private static void copyDirectory(File source, File target) throws IOException {
        if (!target.exists()) {
            if (!target.mkdirs()) {
                throw new IOException("Failed to create target directory: " + target);
            }
        }
        File[] files = source.listFiles();
        if (files != null) {
            for (File file : files) {
                File destFile = new File(target, file.getName());
                if (file.isDirectory()) {
                    copyDirectory(file, destFile);
                } else {
                    Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }
}
