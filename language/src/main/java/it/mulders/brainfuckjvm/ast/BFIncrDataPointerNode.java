package it.mulders.brainfuckjvm.ast;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

public class BFIncrDataPointerNode extends BFCommandNode {
    public BFIncrDataPointerNode(final int sourceCharIndex, final int sourceLength, final FrameSlot dataPointerSlot) {
        super(sourceCharIndex, sourceLength, dataPointerSlot);
    }

    @Override
    public void execute(final VirtualFrame frame) {
        final int dataPointer = getDataPointer(frame);
        setDataPointer(frame, dataPointer + 1);
    }

    @Override
    public String toString() {
        return "INCR_PTR";
    }
}
