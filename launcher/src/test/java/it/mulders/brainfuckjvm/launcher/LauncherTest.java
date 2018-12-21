package it.mulders.brainfuckjvm.launcher;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static it.mulders.brainfuckjvm.launcher.Launcher.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class LauncherTest {
    @Test
    void params_start_with_double_dash() {
        assertThat(isOption("--inspect"), is(true));
    }

    @Test
    void args_dont_start_with_double_dash() {
        assertThat(isOption("bla.bf"), is(false));
    }

    @Test
    void option_without_value_is_true() {
        final Map<String, String> options = parseOption("--inspect");
        final Map.Entry<String, String> option = options.entrySet().iterator().next();
        assertThat(option.getKey(), is("inspect"));
        assertThat(option.getValue(), is("true"));
    }

    @Test
    void option_with_value_keeps_value() {
        final Map<String, String> options = parseOption("--inspect=localhost:2345");
        final Map.Entry<String, String> option = options.entrySet().iterator().next();
        assertThat(option.getKey(), is("inspect"));
        assertThat(option.getValue(), is("localhost:2345"));
    }

    @Test
    void non_existing_source_exit_code_nonzero() throws IOException {
        assertThat(run("doesnt-exist.bf"), is(EXIT_NO_SOURCE));
    }

    @Test
    void existing_source_exit_code_zero() throws IOException {
        assertThat(run("../language/src/test/resources/hello.bf"), is(0));
    }

    @Test
    @Disabled("See question in https://github.com/graalvm/simplelanguage/issues/60")
    void option_with_subvalue_keeps_value() {
        final Map<String, String> options = parseOption("--inspect.WaitAttached=false");
        final Map.Entry<String, String> option = options.entrySet().iterator().next();
        assertThat(option.getKey(), is("WaitAttached"));
        assertThat(option.getValue(), is("false"));
    }
}