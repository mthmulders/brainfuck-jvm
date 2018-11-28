package it.mulders.brainfuckjvm.ast;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.source.SourceSection;
import it.mulders.brainfuckjvm.BrainfuckContext;
import it.mulders.brainfuckjvm.BrainfuckLanguage;

import java.io.PrintWriter;

/**
 * Represents the "Output Byte" command.
 */
public class BFOutputByteNode extends BFCommandNode {
    public BFOutputByteNode(final SourceSection sourceSection, final FrameSlot dataPointerSlot) {
        super(sourceSection, dataPointerSlot);
    }

    @Override
    public void execute(final VirtualFrame frame) {
        final byte currentValue = getCurrentByte(frame);
        doPrint(getContext().getOutput(), (char) currentValue);
    }

    @TruffleBoundary
    private static void doPrint(final PrintWriter out, final char value) {
        out.print(value);
        out.flush();
    }

    private BrainfuckContext getContext() {
        return getRootNode().getLanguage(BrainfuckLanguage.class).getContextReference().get();
    }

    @Override
    public String toString() {
        return "OUTPUT";
    }
}
