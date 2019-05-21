package it.mulders.brainfuckjvm;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Source;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.net.URL;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class ErrorSamplesIT {
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

                assertThat(pe.getMessage(), is(message));
            }
        }
    }
}
