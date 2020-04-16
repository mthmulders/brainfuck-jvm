package it.mulders.brainfuckjvm.demoapp;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExampleTest {
    @Test
    public void yes() {
        assertThat(true).isTrue();
    }
    @Test
    public void no() {
        assertThat(true).isFalse();
    }
}
