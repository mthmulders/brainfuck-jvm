package it.mulders.brainfuckjvm;

import com.oracle.truffle.api.TruffleException;
import com.oracle.truffle.api.nodes.Node;
import lombok.Getter;

/**
 * This exception signals that a Brainfuck program attempted to read a memory location that does not exist.
 */
@Getter
public class InvalidMemoryAccessException extends RuntimeException implements TruffleException {
    private final transient Node location;

    public InvalidMemoryAccessException(final String message, final Node location) {
        super(message);
        this.location = location;
    }

    private static final String MESSAGE = "Invalid memory location (%d) accessed";

    private static InvalidMemoryAccessException create(final Node operation, final String message) {
        return new InvalidMemoryAccessException(message, operation);
    }

    public static InvalidMemoryAccessException memoryOverflow(final Node operation, int dataPointer) {
        return create(operation, String.format(MESSAGE, dataPointer));
    }

    public static InvalidMemoryAccessException memoryUnderflow(final Node operation, int dataPointer) {
        return create(operation, String.format(MESSAGE, dataPointer));
    }
}
