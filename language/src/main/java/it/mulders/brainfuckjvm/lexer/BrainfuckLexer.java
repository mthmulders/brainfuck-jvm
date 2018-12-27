package it.mulders.brainfuckjvm.lexer;

import com.oracle.truffle.api.source.Source;
import it.mulders.brainfuckjvm.lexer.BrainfuckToken.TokenType;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Converts a sequence of characters into a sequence of {@link BrainfuckToken tokens}.
 */
public class BrainfuckLexer {
    /**
     * Parses the source code a sequence of statements.
     * @param source The input program
     * @return A sequence of tokens recognised in the input program.
     */
    public Stream<BrainfuckToken> parse(final Source source) {
        final CharSequence characters = source.getCharacters();
        return IntStream
                .range(0, source.getLength())
                .mapToObj(i -> TokenType.findToken(characters.charAt(i)).map(type -> new BrainfuckToken(i, type)))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

}
