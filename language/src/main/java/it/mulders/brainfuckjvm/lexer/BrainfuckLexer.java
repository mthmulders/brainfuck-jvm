package it.mulders.brainfuckjvm.lexer;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Converts a sequence of characters into a sequence of {@link BrainfuckToken tokens}.
 */
public class BrainfuckLexer {
    private Optional<BrainfuckToken.TokenType> parseChar(final char input) {
        return BrainfuckToken.TokenType.findToken(input);
    }

    private Optional<BrainfuckToken> parseSection(final SourceSection section) {
        return parseChar(section.getCharacters().charAt(0))
                .map(type -> new BrainfuckToken(section, type));
    }

    /**
     * Parses the source code a sequence of statements.
     * @param source The input program
     * @return A sequence of tokens recognised in the input program.
     */
    public Stream<BrainfuckToken> parse(final Source source) {
        return IntStream.range(0, source.getLength())
                .mapToObj(i -> source.createSection(i, 1))
                .map(this::parseSection)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

}
