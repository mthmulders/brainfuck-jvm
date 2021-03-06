package it.mulders.brainfuckjvm.simpleparser.lexer;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

import static it.mulders.brainfuckjvm.Constants.TOKEN_LENGTH;

@AllArgsConstructor
public class BrainfuckToken {
    /**
     * All tokens known in the Brainfuck language.
     */
    @AllArgsConstructor
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

        public static Optional<TokenType> findType(final char command) {
            return Arrays.stream(TokenType.values())
                    .filter(token -> token.command == command)
                    .findAny();
        }
    }

    public final int sourceCharIndex;
    public final TokenType token;

    @Override
    public String toString() {
        return String.format("%s [%d-%d]", this.token.name(), this.sourceCharIndex, TOKEN_LENGTH);
    }
}
