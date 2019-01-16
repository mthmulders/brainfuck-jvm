package it.mulders.brainfuckjvm.parser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;
import it.mulders.brainfuckjvm.BrainfuckLanguage;
import it.mulders.brainfuckjvm.ast.*;
import it.mulders.brainfuckjvm.lexer.BrainfuckToken;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

import static it.mulders.brainfuckjvm.Constants.BF_MEM_SIZE;
import static it.mulders.brainfuckjvm.Constants.TOKEN_LENGTH;
import static it.mulders.brainfuckjvm.lexer.BrainfuckToken.TokenType.*;
import static java.util.logging.Level.SEVERE;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Log
public class BrainfuckParser {
    private static final String DATA_POINTER = "__dataPointer";
    private static final int MEMORY_SIZE = Integer.parseInt(System.getProperty(BF_MEM_SIZE, "30000"));
    private static final int SOURCE_START_INDEX = 0;

    private final BrainfuckLanguage language;
    private final BFVisualizer visualizer = new BFVisualizer();

    /**
     * Build an {@link BFCommandNode AST node} from a {@link BrainfuckToken recognized token}.
     * @param source The source code that contained this token.
     * @param token The recognized token in the original source code.
     * @param pointer The {@link FrameSlot command pointer}
     * @return a node for the Brainfuck AST.
     * @throws BFParseError when the token is not recognized.
     */
    private BFCommandNode parse(final Source source, final BrainfuckToken token, final FrameSlot pointer) {
        final int sourceCharIndex = token.sourceCharIndex;

        switch (token.token) {
            case DECREMENT_BYTE:         return new BFDecrementByteNode(sourceCharIndex, TOKEN_LENGTH, pointer);
            case DECREMENT_DATA_POINTER: return new BFDecrDataPointerNode(sourceCharIndex, TOKEN_LENGTH, pointer);
            case INCREMENT_BYTE:         return new BFIncrementByteNode(sourceCharIndex, TOKEN_LENGTH, pointer);
            case INCREMENT_DATA_POINTER: return new BFIncrDataPointerNode(sourceCharIndex, TOKEN_LENGTH, pointer);
            case INPUT_BYTE:             return new BFInputByteNode(sourceCharIndex, TOKEN_LENGTH, pointer);
            case JUMP_FORWARD:           return new BFJumpNode(sourceCharIndex, TOKEN_LENGTH, pointer);
            case OUTPUT_BYTE:            return new BFOutputByteNode(sourceCharIndex, TOKEN_LENGTH, pointer);

            default:
                log.log(SEVERE, "Unexpected token in source code: %s", token.token);
                final String message = String.format("Unexpected token \"%s\"in source code", token.token);
                throw parseError(source, token, message);
        }
    }

    public BFRootNode parse(final Source source, final Stream<BrainfuckToken> tokens) {
        final Deque<BFJumpNode> jumps = new ArrayDeque<>();

        final FrameDescriptor descriptor = new FrameDescriptor();

        // Describe our memory layout: one array of 30.000 bytes and one position to store the data pointer.
        IntStream.range(0, MEMORY_SIZE)
                .forEach(i -> descriptor.addFrameSlot(i, FrameSlotKind.Byte));
        final FrameSlot dataPointerSlot = descriptor.findOrAddFrameSlot(DATA_POINTER, FrameSlotKind.Int);

        final BFCommandNode[] commands = buildNodes(source, tokens.collect(toList()), dataPointerSlot, jumps);

        final SourceSection section = source.createSection(SOURCE_START_INDEX, source.getLength());
        final BFRootNode root = new BFRootNode(language, descriptor, commands, section);
        root.getChildNodes()[0].markAsRoot();

        if ("true".equals(System.getProperty("brainfuck.ast.dump"))) {
            visualizer.dumpTree("source.bf", "output", root);
        }

        return root;
    }

    private BFCommandNode[] buildNodes(final Source source,
                                       final List<BrainfuckToken> tokens,
                                       final FrameSlot dataPointerSlot,
                                       final Deque<BFJumpNode> jumps) {
        final List<BFCommandNode> nodes = new ArrayList<>(tokens.size());

        for (final BrainfuckToken token : tokens) {
            if (token.token == JUMP_BACKWARD) {
                 if(jumps.isEmpty()) {
                     throw parseError(source, token, "Found ] without matching [");
                 } else {
                     jumps.pop();
                     continue;
                 }
            }

            final BFCommandNode command = parse(source, token, dataPointerSlot);

            if (jumps.isEmpty()) {
                nodes.add(command);
            }
            if (!jumps.isEmpty()) {
                // We're inside a [ jump forward loop. This new command doesn't belong to the root node,
                // but is a child of the innermost [ jump forward.
                jumps.peek().addChild(command);
            }
            if (command instanceof BFJumpNode) {
                jumps.push((BFJumpNode) command);
            }
        }

        if (!jumps.isEmpty()) {
            final SourceSection section = jumps.peek().getSourceSection();
            final int sourceCharIndex = section.getStartColumn();
            final int sourceCharLength = section.getCharLength();
            throw parseError(source, sourceCharIndex, sourceCharLength, "Found [ without matching ]");
        }

        return nodes.toArray(new BFCommandNode[nodes.size()]);
    }

    private BFParseError parseError(final Source source,
                                    final BrainfuckToken token,
                                    final String message) {
        return new BFParseError(source, token.sourceCharIndex, TOKEN_LENGTH, message);
    }
    private BFParseError parseError(final Source source,
                                    final int sourceCharIndex,
                                    final int sourceLength,
                                    final String message) {
        return new BFParseError(source, sourceCharIndex, sourceLength, message);
    }
}
