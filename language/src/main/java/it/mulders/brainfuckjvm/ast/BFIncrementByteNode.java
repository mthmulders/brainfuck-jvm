package it.mulders.brainfuckjvm.ast;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Implements the "Increment Byte" command.
 */
public class BFIncrementByteNode extends BFCommandNode {
    public BFIncrementByteNode(final int sourceCharIndex, final int sourceLength, final FrameSlot dataPointerSlot, final FrameSlot[] slots) {
        super(sourceCharIndex, sourceLength, dataPointerSlot, slots);
    }

    @Override
    public void execute(final VirtualFrame frame) {
        final byte currentValue = getCurrentByte(frame);
        final byte newValue = (byte) (1 + currentValue);
        frame.setByte(getSlot(frame), newValue);
    }

    @Override
    public String toString() {
        return "INCR_VAL";
    }

}
