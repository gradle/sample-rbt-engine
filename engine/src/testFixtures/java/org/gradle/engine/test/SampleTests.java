package org.gradle.engine.test;

import org.junit.jupiter.api.Test;

public final class SampleTests {
    @Test
    public void passingTest() {}

    @Test
    public void failingTest() {
        throw new RuntimeException("Failing test fails...");
    }
}
