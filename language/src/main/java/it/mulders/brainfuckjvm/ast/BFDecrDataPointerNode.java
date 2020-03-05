package it.mulders.brainfuckjvm.ast;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

public class BFDecrDataPointerNode extends BFCommandNode {
    public BFDecrDataPointerNode(final int sourceCharIndex, final int sourceLength, final FrameSlot dataPointerSlot, final FrameSlot[] slots) {
        super(sourceCharIndex, sourceLength, dataPointerSlot, slots);
    }

    @Override
    public void execute(final VirtualFrame frame) {
        final int dataPointer = getDataPointer(frame);
        setDataPointer(frame, dataPointer - 1);
    }

    @Override
    public String toString() {
        return "DECR_PTR";
    }
}
