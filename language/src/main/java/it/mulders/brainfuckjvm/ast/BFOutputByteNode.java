package it.mulders.brainfuckjvm.ast;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import it.mulders.brainfuckjvm.BrainfuckContext;
import it.mulders.brainfuckjvm.BrainfuckLanguage;

import java.io.PrintWriter;

/**
 * Represents the "Output Byte" command.
 */
public class BFOutputByteNode extends BFCommandNode {
    public BFOutputByteNode(final int sourceCharIndex, final int sourceLength, final FrameSlot dataPointerSlot, final FrameSlot[] slots) {
        super(sourceCharIndex, sourceLength, dataPointerSlot, slots);
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
        return lookupContextReference(BrainfuckLanguage.class).get();
    }

    @Override
    public String toString() {
        return "OUTPUT";
    }
}
