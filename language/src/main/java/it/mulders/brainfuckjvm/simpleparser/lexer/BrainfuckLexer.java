package it.mulders.brainfuckjvm.simpleparser.lexer;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.oracle.truffle.api.source.Source;
import it.mulders.brainfuckjvm.simpleparser.lexer.BrainfuckToken.TokenType;

/**
 * Converts a sequence of characters into a sequence of {@link BrainfuckToken tokens}.
 */
public class BrainfuckLexer {

    private Optional<BrainfuckToken> parseCharacter(final CharSequence characters, final int index) {
        return TokenType.findType(characters.charAt(index))
                .map(type -> new BrainfuckToken(index, type));
    }

    private Stream<BrainfuckToken> parse(final Source source, final CharSequence characters) {
        return IntStream.range(0, source.getLength())
                .mapToObj(index -> parseCharacter(characters, index))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    /**
     * Parses the source code a sequence of statements.
     * @param source The input program
     * @return A sequence of tokens recognised in the input program.
     */
    public Stream<BrainfuckToken> parse(final Source source) {
        final CharSequence characters = source.getCharacters();
        return parse(source, characters);
    }
}
