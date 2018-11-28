package it.mulders.brainfuckjvm.parser;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.source.SourceSection;
import it.mulders.brainfuckjvm.BrainfuckLanguage;
import it.mulders.brainfuckjvm.ast.*;
import it.mulders.brainfuckjvm.lexer.BrainfuckToken;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static it.mulders.brainfuckjvm.lexer.BrainfuckToken.TokenType.*;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Slf4j
public class BrainfuckParser {
    private static final String DATA_POINTER = "__dataPointer";
    private static final int MEMORY_SIZE = 30_000;

    private final BrainfuckLanguage language;
    private final BFVisualizer visualizer = new BFVisualizer();

    /**
     * Build an {@link BFCommandNode AST node} from a {@link BrainfuckToken recognized token}.
     * @param token The recognized token in the original source code.
     * @param pointer The {@link FrameSlot command pointer}
     * @return a node for the Brainfuck AST.
     * @throws BFParseError when the token is not recognized.
     */
    private BFCommandNode parse(final BrainfuckToken token, final FrameSlot pointer) {
        final SourceSection sourceSection = token.sourceSection;

        BFCommandNode result;
        switch (token.token) {
            case DECREMENT_BYTE:         result = new BFDecrementByteNode(sourceSection, pointer);   break;
            case DECREMENT_DATA_POINTER: result = new BFDecrDataPointerNode(sourceSection, pointer); break;
            case INCREMENT_BYTE:         result = new BFIncrementByteNode(sourceSection, pointer);   break;
            case INCREMENT_DATA_POINTER: result = new BFIncrDataPointerNode(sourceSection, pointer); break;
            case INPUT_BYTE:             result = new BFInputByteNode(sourceSection, pointer);       break;
            case JUMP_FORWARD:           result = new BFJumpNode(sourceSection, pointer);            break;
            case OUTPUT_BYTE:            result = new BFOutputByteNode(sourceSection, pointer);      break;

            default:
                log.error("Unexpected token in source code: {}", token.token);
                final String message = String.format("Unexpected token \"%s\"in source code", token.token);
                throw parseError(token.sourceSection, message);
        }

        return result;
    }

    public BFRootNode parse(final SourceSection source, final Stream<BrainfuckToken> tokens) {
        final Stack<BFJumpNode> jumps = new Stack<>();

        final FrameDescriptor descriptor = new FrameDescriptor();

        // Describe our memory layout: one array of 30.000 bytes and one position to store the data pointer.
        IntStream.range(0, MEMORY_SIZE)
                .forEach(i -> descriptor.addFrameSlot(i, FrameSlotKind.Byte));
        final FrameSlot dataPointerSlot = descriptor.findOrAddFrameSlot(DATA_POINTER, FrameSlotKind.Int);

        final BFCommandNode[] commands = buildNodes(tokens.collect(toList()), dataPointerSlot, jumps);

        final BFRootNode root = new BFRootNode(language, descriptor, source, commands);

        if ("true".equals(System.getProperty("brainfuck.ast.dump"))) {
            visualizer.dumpTree("source.bf", "output", root);
        }

        return root;
    }

    private BFCommandNode[] buildNodes(final List<BrainfuckToken> tokens,
                                       final FrameSlot dataPointerSlot,
                                       final Stack<BFJumpNode> jumps) {
        final List<BFCommandNode> nodes = new ArrayList<>(tokens.size());

        for (final BrainfuckToken token : tokens) {
            if (token.token == JUMP_BACKWARD) {
                 if(jumps.empty()) {
                     throw parseError(token.sourceSection, "Found ] without matching [");
                 } else {
                     jumps.pop();
                     continue;
                 }
            }

            final BFCommandNode command = parse(token, dataPointerSlot);

            if (jumps.empty()) {
                nodes.add(command);
            }
            if (!jumps.empty()) {
                // We're inside a [ jump forward loop. This new command doesn't belong to the root node,
                // but is a child of the innermost [ jump forward.
                jumps.peek().addChild(command);
            }
            if (command instanceof BFJumpNode) {
                jumps.push((BFJumpNode) command);
            }
        }

        if (!jumps.empty()) {
            final BFJumpNode jumpForward = jumps.peek();
            throw parseError(jumpForward.getSourceSection(), "Found [ without matching ]");
        }

        return nodes.toArray(new BFCommandNode[nodes.size()]);
    }

    private BFParseError parseError(final SourceSection section, final String message) {
        return new BFParseError(
                section.getSource(),
                section.getStartLine(),
                section.getStartColumn(),
                section.getCharLength(),
                message
        );
    }
}
