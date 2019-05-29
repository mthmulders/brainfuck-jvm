package it.mulders.brainfuckjvm.simpleparser;

import java.util.stream.Stream;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleException;
import com.oracle.truffle.api.source.Source;
import it.mulders.brainfuckjvm.BrainfuckLanguage;
import it.mulders.brainfuckjvm.ast.BFRootNode;
import it.mulders.brainfuckjvm.simpleparser.lexer.BrainfuckLexer;
import it.mulders.brainfuckjvm.simpleparser.lexer.BrainfuckToken;
import it.mulders.brainfuckjvm.simpleparser.parser.BrainfuckParser;
import lombok.AllArgsConstructor;

/**
 * Simple, regex-based parsing facade for the Brainfuck language.
 * @see <a href="https://www.xkcd.com/1171/">Perl Problems</a>
 */
@AllArgsConstructor
public class SimpleParser {
    /** The {@link BrainfuckLanguage} instance that generated call targets will be associated with. */
    private final BrainfuckLanguage language;

    /**
     * Reads a Truffle source representation and builds a CallTarget to invoke it.
     * @param source The Truffle source representation to parse.
     * @throws TruffleException When the process fails.
     * @return A {@link CallTarget} to invoke the parsed source code.
     */
    public CallTarget parseSource(final Source source) {
        final BrainfuckLexer lexer = new BrainfuckLexer();
        final Stream<BrainfuckToken> tokens = lexer.parse(source);

        final BrainfuckParser parser = new BrainfuckParser(language);
        final BFRootNode rootNode = parser.parse(source, tokens);

        return Truffle.getRuntime().createCallTarget(rootNode);
    }
}
