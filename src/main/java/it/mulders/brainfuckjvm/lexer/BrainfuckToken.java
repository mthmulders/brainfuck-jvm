package it.mulders.brainfuckjvm.lexer;

import com.oracle.truffle.api.source.SourceSection;

import java.util.Arrays;
import java.util.Optional;

public class BrainfuckToken {
    /**
     * All tokens known in the Brainfuck language.
     */
    public enum TokenType {
        DECREMENT_BYTE('-'),
        DECREMENT_DATA_POINTER('<'),
        INCREMENT_BYTE('+'),
        INCREMENT_DATA_POINTER('>'),
        INPUT_BYTE(','),
        JUMP_BACKWARD(']'),
        JUMP_FORWARD('['),
        OUTPUT_BYTE('.');

        private char command;

        TokenType(final char command) {
            this.command = command;
        }

        public static Optional<TokenType> findToken(final char command) {
            return Arrays.stream(TokenType.values())
                    .filter(token -> token.command == command)
                    .findAny();
        }
    }

    public final SourceSection sourceSection;
    public final TokenType token;

    public BrainfuckToken(final SourceSection sourceSection, final TokenType token) {
        this.sourceSection = sourceSection;
        this.token = token;
    }

    @Override
    public String toString() {
        return String.format("%s [%d:%d]", this.token.name(), this.sourceSection.getStartLine(), this.sourceSection.getStartColumn());
    }
}
