package org.gradle.rbt.descriptor;

import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;

import java.io.File;

public final class ResourceBasedTestDescriptor extends AbstractTestDescriptor {
    private final File file;
    private final String name;

    public ResourceBasedTestDescriptor(UniqueId parentId, File file, String name) {
        super(parentId.append("testDefinitionFile", file.getName()).append("testDefinition", name), file.getName() + " : " + name);
        this.file = file;
        this.name = name;
    }

    @Override
    public Type getType() {
        return Type.TEST;
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Test [file=" + file.getName() + ", name=" + name + "]";
    }
}
