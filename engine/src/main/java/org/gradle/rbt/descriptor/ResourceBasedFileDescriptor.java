package org.gradle.rbt.descriptor;

import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;

import java.io.File;

public class ResourceBasedFileDescriptor extends AbstractTestDescriptor {
    private static final String SEGMENT_TYPE = "testDefinitionFile";

    private final File file;

    public ResourceBasedFileDescriptor(UniqueId parentId, File file) {
        super(parentId.append(SEGMENT_TYPE, file.getName()), file.getName());
        this.file = file;
    }

    @Override
    public Type getType() {
        return Type.CONTAINER;
    }

    public File getFile() {
        return file;
    }
}
