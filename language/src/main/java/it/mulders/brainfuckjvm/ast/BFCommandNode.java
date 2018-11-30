package it.mulders.brainfuckjvm.ast;

import com.oracle.truffle.api.source.SourceSection;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameUtil;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NodeInfo(description = "The abstract base node for all expressions")
public abstract class BFCommandNode extends Node {
    /** Reference to the source code snippet where this node originates from. */
    @Getter
    private final SourceSection sourceSection;

    /** The variable that determines whether the inner loop will be executed the first time. */
    @Getter
    private final FrameSlot dataPointerSlot;

    public abstract void execute(final VirtualFrame frame);

    protected final void setDataPointer(final VirtualFrame frame, final int newDataPointer) {
        frame.setInt(dataPointerSlot, newDataPointer);
    }

    protected final int getDataPointer(final VirtualFrame frame) {
        return FrameUtil.getIntSafe(frame, dataPointerSlot);
    }

    protected final FrameSlot getSlot(final VirtualFrame frame) {
        final int dataPointer = getDataPointer(frame);
        return frame.getFrameDescriptor().findFrameSlot(dataPointer);
    }

    protected final byte getCurrentByte(final VirtualFrame frame) {
        final FrameSlot currentByteSlot = getSlot(frame);
        return FrameUtil.getByteSafe(frame, currentByteSlot);
    }
}
