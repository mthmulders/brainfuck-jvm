package it.mulders.brainfuckjvm.lexer;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Converts a sequence of characters into a sequence of {@link BrainfuckToken tokens}.
 */
public class BrainfuckLexer {
    private Optional<BrainfuckToken.TokenType> parseChar(final char input) {
        return BrainfuckToken.TokenType.findToken(input);
    }

    private Optional<BrainfuckToken> parseSection(final Source source, final CharSequence characters, final int i) {
        return parseChar(characters.charAt(i))
                .map(type -> new BrainfuckToken(i, 1, type));
    }

    /**
     * Parses the source code a sequence of statements.
     * @param source The input program
     * @return A sequence of tokens recognised in the input program.
     */
    public Stream<BrainfuckToken> parse(final Source source) {
        final CharSequence characters = source.getCharacters();
        return IntStream.range(0, source.getLength())
                .mapToObj(i -> this.parseSection(source, characters, i))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

}
