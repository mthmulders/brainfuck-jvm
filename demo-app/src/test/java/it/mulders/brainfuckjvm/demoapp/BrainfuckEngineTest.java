package it.mulders.brainfuckjvm.demoapp;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BrainfuckEngineTest {
    private BrainfuckEngine engine = new BrainfuckEngine();

    @Test
    void should_run_example_program() {
        final String input = "+++++ +++++ ."; // Should print the character 10

        final ExecutionResult result = engine.runProgram(input);

        assertThat(result.getErrorMessage()).isNull();
        assertThat(result.getOutput()).isEqualTo("\n");
    }
}