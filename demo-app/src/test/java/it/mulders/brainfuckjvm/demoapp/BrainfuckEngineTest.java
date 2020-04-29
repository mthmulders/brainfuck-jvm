package it.mulders.brainfuckjvm.demoapp;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

public class BrainfuckEngineTest implements WithAssertions {
    private BrainfuckEngine engine = new BrainfuckEngine();

    @Test
    void should_run_example_program() {
        final String input = "+++++ +++++ ."; // Should print the character 10

        final ExecutionResult result = engine.runProgram(input);

        assertThat(result.getErrorMessage()).isNull();
        assertThat(result.getOutput()).isEqualTo("\n");
    }
}