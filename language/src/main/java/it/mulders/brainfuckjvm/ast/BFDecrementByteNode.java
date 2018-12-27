package it.mulders.brainfuckjvm.ast;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Represents the "Decrement Byte" command.
 */
public class BFDecrementByteNode extends BFCommandNode {
    public BFDecrementByteNode(final int sourceCharIndex, final int sourceLength, final FrameSlot dataPointerSlot) {
        super(sourceCharIndex, sourceLength, dataPointerSlot);
    }

    @Override
    public void execute(final VirtualFrame frame) {
        final byte currentValue = getCurrentByte(frame);
        final byte newValue = (byte) (currentValue - 1);
        frame.setByte(getSlot(frame), newValue);
    }

    @Override
    public String toString() {
        return "DECR_VAL";
    }

}
