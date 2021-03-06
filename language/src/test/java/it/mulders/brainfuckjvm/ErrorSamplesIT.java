package it.mulders.brainfuckjvm;

import org.assertj.core.api.WithAssertions;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Source;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.util.stream.Collectors;

public class ErrorSamplesIT implements WithAssertions {
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();

    private InputStream readOptionalFile(final String filename) throws IOException {
        final URL url = this.getClass().getClassLoader().getResource(filename);
        if (url == null) {
            return new ByteArrayInputStream(new byte[0]);
        }
        return url.openStream();
    }

    private InputStream readFile(final String filename) throws IOException {
        final InputStream stream = readOptionalFile(filename);
        if (stream == null) {
            fail(String.format("Could not load file %s", filename));
        }
        return stream;
    }

    @ParameterizedTest
    @ValueSource(strings = { "hello-error" })
    void executeTest(final String testcase) throws IOException {
        final String sourceFile = String.format("%s.bf", testcase);
        final String inputFile = String.format("%s.in", testcase);
        final String outputFile = String.format("%s.out", testcase);

        final BufferedReader expected = new BufferedReader(new InputStreamReader(readFile(outputFile)));

        try (final InputStreamReader input = new InputStreamReader(readFile(sourceFile))) {
            final Source source = Source.newBuilder(BrainfuckLanguage.ID, input, null)
                    .cached(false)
                    .buildLiteral();

            try (final Engine engine = Engine.newBuilder().in(readOptionalFile(inputFile)).out(out).err(err).build();
                 final Context context = Context.newBuilder().engine(engine).build()) {
                context.eval(source);
                fail("No error happened, but it was expected");
            } catch (PolyglotException pe) {
                final String message = expected.lines().collect(Collectors.joining("\n"));

                assertThat(pe.getMessage()).isEqualTo(message);
            }
        }
    }

    @Test
    void invalid_memory_access_before_zero() {
        final Source source = Source.newBuilder(BrainfuckLanguage.ID, "<+", null)
                .cached(false)
                .buildLiteral();
        try (final Engine engine = Engine.newBuilder().build();
             final Context context = Context.newBuilder().engine(engine).build()) {
            context.eval(source);
            fail("No error happened, but it was expected");
        } catch (final PolyglotException pe) {
            assertThat(pe.isInternalError()).isFalse();
            assertThat(pe.getMessage()).contains("Invalid memory location (-1) accessed");
        }
    }

    @Test
    void invalid_memory_access_beyond_max() {
        System.setProperty("bf.mem.size", "1");
        final Source source = Source.newBuilder(BrainfuckLanguage.ID, ">+", null)
                .cached(false)
                .buildLiteral();
        try (final Engine engine = Engine.newBuilder().build();
             final Context context = Context.newBuilder().engine(engine).build()) {
            context.eval(source);
            fail("No error happened, but it was expected");
        } catch (final PolyglotException pe) {
            assertThat(pe.isInternalError()).isFalse();
            assertThat(pe.getMessage()).contains("Invalid memory location (1) accessed");
        } finally {
            System.clearProperty("bf.mem.size");
        }
    }
}
