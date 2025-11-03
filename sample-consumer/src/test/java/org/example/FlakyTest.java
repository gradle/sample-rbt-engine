package org.example;

import org.junit.jupiter.api.Test;

import java.util.Random;

public class FlakyTest {
    @Test
    public void flaky() {
        if (new Random().nextInt(10) > 5) {
            System.out.println("Flakey test succeeded");
        } else {
            throw new RuntimeException("Flakey test failed");
        }
    }
}
