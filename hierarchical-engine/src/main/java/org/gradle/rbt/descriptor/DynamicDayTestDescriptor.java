package org.gradle.rbt.descriptor;

import org.gradle.rbt.engine.EmptyContext;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.hierarchical.Node;

import java.io.File;
import java.time.LocalDate;

public final class DynamicDayTestDescriptor extends AbstractTestDescriptor implements Node<EmptyContext> {
    public static final Logger LOGGER = LoggerFactory.getLogger(DynamicDayTestDescriptor.class);

    private final File file;
    private final LocalDate date;

    public DynamicDayTestDescriptor(UniqueId uniqueId, String displayName, File file, LocalDate date) {
        super(uniqueId, displayName);
        this.file = file;
        this.date = date;
    }

    @Override
    public Type getType() {
        return Type.TEST;
    }

    @Override
    public EmptyContext execute(EmptyContext context, DynamicTestExecutor dynamicTestExecutor) {
        LOGGER.info(() -> "Executing dynamic test in file: " + file.getName() + ", for date: " + date);
        return context;
    }
}
