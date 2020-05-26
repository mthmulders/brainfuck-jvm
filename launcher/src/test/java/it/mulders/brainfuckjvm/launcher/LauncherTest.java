package it.mulders.brainfuckjvm.launcher;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static it.mulders.brainfuckjvm.launcher.Launcher.isOption;
import static it.mulders.brainfuckjvm.launcher.Launcher.parseOption;
import static it.mulders.brainfuckjvm.launcher.Launcher.run;
import static it.mulders.brainfuckjvm.launcher.Launcher.EXIT_NO_SOURCE;

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
    void non_existing_source_exit_code_nonzero() throws IOException {
        assertThat(run("doesnt-exist.bf")).isEqualTo(EXIT_NO_SOURCE);
    }

    @Test
    void existing_source_exit_code_zero() throws IOException {
        assertThat(run("../language/src/test/resources/hello.bf")).isEqualTo(0);
    }

    @Test
    @Disabled("See question in https://github.com/graalvm/simplelanguage/issues/60")
    void option_with_subvalue_keeps_value() {
        final Map<String, String> options = parseOption("--inspect.WaitAttached=false");
        final Map.Entry<String, String> option = options.entrySet().iterator().next();
        assertThat(option.getKey()).isEqualTo("WaitAttached");
        assertThat(option.getValue()).isEqualTo("false");
    }
}