package it.mulders.brainfuckjvm.ast;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.instrumentation.InstrumentableNode;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.Source;
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
    private static final int NO_SOURCE = -1;
    private static final int UNAVAILABLE_SOURCE = -2;

    protected final int sourceCharIndex;
    protected final int sourceLength;

    /** The slot that holds the data pointer. */
    @Getter
    protected final FrameSlot dataPointerSlot;

    /**
     * Overloaded constructor for nodes that do not have a source section.
     * @param dataPointerSlot The slot that holds the data pointer.
     */
    public BFCommandNode(final FrameSlot dataPointerSlot) {
        this(NO_SOURCE, 0, dataPointerSlot);
    }

    /**
     * The creation of source section can be implemented lazily by looking up the root node source
     * and then creating the source section object using the indices stored in the node. This avoids
     * the eager creation of source section objects during parsing and creates them only when they
     * are needed. Alternatively, if the language uses source sections to implement language
     * semantics, then it might be more efficient to eagerly create source sections and store it in
     * the AST.
     *
     * For more details see {@link InstrumentableNode}.
     */
    @Override
    @TruffleBoundary
    public final SourceSection getSourceSection() {
        if (sourceCharIndex == NO_SOURCE) {
            // AST node without source
            return null;
        }
        final RootNode rootNode = getRootNode();
        if (rootNode == null) {
            // not yet adopted yet
            return null;
        }
        final SourceSection rootSourceSection = rootNode.getSourceSection();
        if (rootSourceSection == null) {
            return null;
        }
        final Source source = rootSourceSection.getSource();
        if (sourceCharIndex == UNAVAILABLE_SOURCE) {
            return source.createUnavailableSection();
        } else {
            return source.createSection(sourceCharIndex, sourceLength);
        }
    }

    public abstract void execute(final VirtualFrame frame);

    final void setDataPointer(final VirtualFrame frame, final int newDataPointer) {
        frame.setInt(dataPointerSlot, newDataPointer);
    }

    final int getDataPointer(final VirtualFrame frame) {
        return FrameUtil.getIntSafe(frame, dataPointerSlot);
    }

    final FrameSlot getSlot(final VirtualFrame frame) {
        final int dataPointer = getDataPointer(frame);
        return frame.getFrameDescriptor().findFrameSlot(dataPointer);
    }

    final byte getCurrentByte(final VirtualFrame frame) {
        final FrameSlot currentByteSlot = getSlot(frame);
        return FrameUtil.getByteSafe(frame, currentByteSlot);
    }
}
