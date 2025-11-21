package org.gradle.rbt.descriptor;

import org.gradle.rbt.engine.EmptyContext;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.hierarchical.Node;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ResourceBasedFileDescriptor extends AbstractTestDescriptor implements Node<EmptyContext> {
    private final File file;

    public ResourceBasedFileDescriptor(UniqueId parentId, File file) {
        super(parentId.append("file", file.getName()), file.getName());
        this.file = file;
    }

    @Override
    public Type getType() {
        return Type.CONTAINER;
    }

    @Override
    public boolean mayRegisterTests() {
        return true;
    }

    @Override
    public EmptyContext execute(EmptyContext context, DynamicTestExecutor dynamicTestExecutor) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        LocalDate date = LocalDate.parse(lines.get(0).trim());
        int numDays = Integer.parseInt(lines.get(1).trim());

        for (int i = 0; i < numDays; i++) {
            LocalDate day = date.plusDays(i);
            String displayName = day.format(DateTimeFormatter.ISO_DATE);
            UniqueId childId = getUniqueId().append("day", day.toString());

            DynamicDayTestDescriptor dayDescriptor = new DynamicDayTestDescriptor(childId, displayName, file, day);
            dayDescriptor.setParent(this);
            dynamicTestExecutor.execute(dayDescriptor);
        }

        return context;
    }
}
