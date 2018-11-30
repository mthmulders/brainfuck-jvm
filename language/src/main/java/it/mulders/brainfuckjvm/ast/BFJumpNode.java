package it.mulders.brainfuckjvm.ast;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.source.SourceSection;

/**
 * Represents a pair of "Jump Forward & Jump Backward" commands.
 * It contains the commands that are embodied inside them.
 */
public class BFJumpNode extends BFCommandNode implements BFParentNode {

    public BFJumpNode(final SourceSection sourceSection, final FrameSlot dataPointerSlot) {
        super(sourceSection, dataPointerSlot);
    }

    @CompilationFinal(dimensions = 1)
    @Children
    private BFCommandNode[] children = new BFCommandNode[0];

    @Override
    public BFCommandNode[] getChildNodes() {
        final BFCommandNode[] result = new BFCommandNode[this.children.length];
        System.arraycopy(this.children, 0, result, 0, this.children.length);
        return result;
    }

    @Override
    public void execute(final VirtualFrame frame) {
        final byte currentByte = getCurrentByte(frame);

        if (currentByte == 0) {
            // jump instruction pointer forward to the command after the matching ] command
            return;
        }

        do {
            // Continue executing the instruction(s) after the [.
            executeChildren(frame);
        } while (shouldRunChildrenAgain(frame));
    }

    private boolean shouldRunChildrenAgain(final VirtualFrame frame) {
        final byte currentByte = getCurrentByte(frame);

        if (currentByte != 0) {
            // jump instruction pointer back to the command after the matching [ command.
            return true;
        } else {
            // move the instruction pointer forward to the next command (that is, after the ] that closes this node).
            return false;
        }
    }

    private void executeChildren(final VirtualFrame frame) {
        for (BFCommandNode child : children) {
            child.execute(frame);
        }
    }

    /**
     * Add a child node to this pair of jump commands.
     * @param command The new child node.
     */
    public void addChild(final BFCommandNode command) {
        final BFCommandNode[] newCommands = new BFCommandNode[this.children.length + 1];
        System.arraycopy(this.children, 0, newCommands, 0, this.children.length);
        newCommands[this.children.length] = command;
        this.children = newCommands;
    }

    @Override
    public String toString() {
        return "JUMP";
    }
}
