package it.mulders.brainfuckjvm.demoapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Source;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BrainfuckEngine {
    private static final String BRAINFUCK_ID = "bf";

    public ExecutionResult runProgram(final String input) {
        final ExecutionResult.ExecutionResultBuilder result = ExecutionResult.builder();
        Source source = null;
        try {
            source = Source.newBuilder(BRAINFUCK_ID, input, "user input").build();
        } catch (IOException ioe) {
            log.error("Could not build representation of Brainfuck source code unit", ioe);
            return result.errorMessage("Could not build representation of Brainfuck source code").build();
        }

        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        final Context.Builder contextBuilder = Context.newBuilder(BRAINFUCK_ID);
        contextBuilder.out(output);

        try (final Context context = contextBuilder.build()) {
            final long start = System.currentTimeMillis();
            context.eval(source);

            result.executionTime(System.currentTimeMillis() - start);
            result.output(output.toString());

        } catch (final IllegalArgumentException iae) {
            // This gets thrown when the Brainfuck language is not installed.
            return handleIllegalArgumentException(result, iae);
        } catch (final PolyglotException ex) {
            return handlePolyglotException(result, ex);
        }


        return result.build();
    }

    private ExecutionResult handlePolyglotException(ExecutionResult.ExecutionResultBuilder result, PolyglotException ex) {
        if (ex.isInternalError()) {
            // for internal errors we print the full stack trace
            ex.printStackTrace();
            final String message = String.format("Could not run Brainfuck program due to %s", ex.getMessage());
            return result.errorMessage(message).build();
        } else {
            return result.errorMessage(ex.getMessage()).build();
        }
    }

    private ExecutionResult handleIllegalArgumentException(ExecutionResult.ExecutionResultBuilder result, IllegalArgumentException iae) {
        if (iae.getMessage().contains("A language with id 'bf' is not installed")) {
            return result.errorMessage(iae.getMessage()).build();
        } else {
            log.error("Unknown technical error", iae);
            return result.errorMessage("Unknown technical error").build();
        }
    }
}
