package it.mulders.brainfuckjvm.launcher;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import static it.mulders.brainfuckjvm.launcher.Launcher.isOption;
import static it.mulders.brainfuckjvm.launcher.Launcher.parseOption;

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
    void option_without_value_is_true() {
        final Map<String, String> options = parseOption("--inspect");
        final Map.Entry<String, String> option = options.entrySet().iterator().next();
        assertThat(option.getKey()).isEqualTo("inspect");
        assertThat(option.getValue()).isEqualTo("true");
    }

    @Test
    void option_with_value_keeps_value() {
        final Map<String, String> options = parseOption("--inspect=localhost:2345");
        final Map.Entry<String, String> option = options.entrySet().iterator().next();
        assertThat(option.getKey()).isEqualTo("inspect");
        assertThat(option.getValue()).isEqualTo("localhost:2345");
    }

    @Test
    @Disabled("See question in https://github.com/graalvm/simplelanguage/issues/60")
    void option_with_subvalue_keeps_value() {
        final Map<String, String> options = parseOption("--inspect.WaitAttached=false");
        final Map.Entry<String, String> option = options.entrySet().iterator().next();
        assertThat(option.getKey()).isEqualTo("WaitAttached");
        assertThat(option.getValue()).isEqualTo("false");
    }

    @Test
    void preprocessArguments_should_move_known_options() {
        final Map<String, String> options = new HashMap<>();
        final List<String> args = Collections.singletonList("--inspect.WaitAttached=false");
        final List<String> remainder = new Launcher().preprocessArguments(args, options);
        assertThat(remainder).isEmpty();
        assertThat(options).containsOnly(entry("inspect.WaitAttached", "false"));
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