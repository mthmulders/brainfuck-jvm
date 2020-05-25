package it.mulders.brainfuckjvm.launcher;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import static it.mulders.brainfuckjvm.launcher.Launcher.isOption;

public class LauncherTest implements WithAssertions {
    @Test
    void params_start_with_double_dash() {
        assertThat(isOption("--inspect")).isTrue();
    }

    @Test
    void args_dont_start_with_double_dash() {
        assertThat(isOption("bla.bf")).isFalse();
    }

    @Test
    void preprocessArguments_should_not_return_filename() {
        final Map<String, String> options = new HashMap<>();
        final List<String> args = Collections.singletonList("foo.bf");
        final List<String> remainder = new Launcher().preprocessArguments(args, options);
        assertThat(options).isEmpty();
        assertThat(remainder).isEmpty();
    }
}