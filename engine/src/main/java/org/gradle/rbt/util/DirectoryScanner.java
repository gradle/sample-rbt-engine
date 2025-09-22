package org.gradle.rbt.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class DirectoryScanner {
    private TestFileParser testFileParser = new TestFileParser();

    public TestFileParser getTestFileParser() {
        return testFileParser;
    }

    public List<File> scanDirectory(File dir) {
        return scanDirectory(dir, false);
    }

    public List<File> scanDirectory(File dir, boolean includeDirs) {
        List<File> filesFound = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    if (includeDirs) {
                        filesFound.add(file);
                    }
                    filesFound.addAll(scanDirectory(file));
                } else if (testFileParser.isValidTestDefinitionFile(file)) {
                    filesFound.add(file);
                }
            }
        }
        return filesFound;
    }
}
