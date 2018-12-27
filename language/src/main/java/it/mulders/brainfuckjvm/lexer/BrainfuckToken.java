package it.mulders.brainfuckjvm.lexer;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
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

    public final int sourceCharIndex;
    public final int sourceLength = 1;
    public final TokenType token;

    @Override
    public String toString() {
        return String.format("%s [%d-%d]", this.token.name(), this.sourceCharIndex, this.sourceLength);
    }
}
