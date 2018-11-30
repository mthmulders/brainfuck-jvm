package it.mulders.brainfuckjvm.ast;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.source.SourceSection;

public class BFIncrDataPointerNode extends BFCommandNode {
    public BFIncrDataPointerNode(final SourceSection sourceSection, final FrameSlot dataPointerSlot) {
        super(sourceSection, dataPointerSlot);
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
