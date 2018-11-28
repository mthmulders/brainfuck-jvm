package it.mulders.brainfuckjvm;

import lombok.extern.slf4j.Slf4j;
import org.graalvm.polyglot.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class Brainfuck_context_spec {
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();

    private void evaluateInput(final String sourcecode) {
        final Source source = Source.newBuilder(BrainfuckLanguage.ID, sourcecode, null).cached(false).buildLiteral();

        try (final Engine engine = Engine.newBuilder().out(out).err(err).build();
             final Context context = Context.newBuilder().engine(engine).build()) {
            context.eval(source);
        }
    }

    @Test
    void can_output_byte() {
        // 33 is !
        final String sourcecode = "++++++++++ ++++++++++ ++++++++++ +++ .";
        evaluateInput(sourcecode);
        assertThat(out.toString(), is("!"));
    }

    @Test
    void can_jump_back() {
        final String sourcecode = "++[-]+.";
        evaluateInput(sourcecode);
        assertThat(out.size(), is(1));
        assertThat(out.toByteArray()[0], is((byte) 1));
    }

    @Test
    void can_jump_forward() {
        final String sourcecode = "[+]++.";
        evaluateInput(sourcecode);
        assertThat(out.size(), is(1));
        assertThat(out.toByteArray()[0], is((byte) 2));
    }

    @Test
    void can_add_2_and_5() {
        // https://en.wikipedia.org/wiki/Brainfuck#Adding_two_values
        final String sourcecode = "++>+++++[<+>-]++++++++[<++++++>-]<.";
        evaluateInput(sourcecode);
        assertThat(out.toString(), is("7"));
    }

    @Test
    void can_run_hello_world() {
        // https://en.wikipedia.org/wiki/Brainfuck#Hello_World!
        final String sourcecode = "++++++++[>++++[>++>+++>+++>+<<<<-]>+>+>->>+[<]<-]>>.>---.+++++++..+++.>>.<-.<.+++.------.--------.>>+.>++.";
        //                         1234567890123456789012345678901234567890123456789012345678901234567890
        //                         0        1         2         3         4
        evaluateInput(sourcecode);
        assertThat(out.toString(), is("Hello World!\n"));
    }

    @Test
    void parsing_fails_when_forward_jump_doesnt_have_backward_jump() {
        final String sourcecode = "[";

        assertThrows(
                PolyglotException.class,
                () -> evaluateInput(sourcecode),
                "Found [ without matching ]");
    }

    @Test
    void parsing_fails_when_more_forward_jumps_than_backward_jumps() {
        final String sourcecode = "[[]";

        assertThrows(
                PolyglotException.class,
                () -> evaluateInput(sourcecode),
                "Found [ without matching ]");
    }

    @Test
    void parsing_fails_when_backward_jump_doesnt_have_forward_jump() {
        final String sourcecode = "]";

        assertThrows(
                PolyglotException.class,
                () -> evaluateInput(sourcecode),
                "Found ] without matching [");
    }

    @Test
    void parsing_fails_when_more_backward_jumps_than_forward_jumps() {
        final String sourcecode = "[]]";

        assertThrows(
                PolyglotException.class,
                () -> evaluateInput(sourcecode),
                "Found ] without matching [");
    }
}
