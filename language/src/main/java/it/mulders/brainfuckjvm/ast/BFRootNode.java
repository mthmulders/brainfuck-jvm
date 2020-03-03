package it.mulders.brainfuckjvm.ast;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.SourceSection;
import it.mulders.brainfuckjvm.BrainfuckLanguage;
import it.mulders.brainfuckjvm.runtime.BFNull;
import lombok.Getter;
import lombok.Setter;

@NodeInfo(
        language = BrainfuckLanguage.ID,
        description = "The root of all Brainfuck execution trees"
)
public class BFRootNode extends RootNode implements BFParentNode {
    /** The source section that all AST nodes will derive their source section from. */
    @Getter
    private final SourceSection sourceSection;

    /** The statements that are executed. */
    @Children
    @Setter
    private BFCommandNode[] children;

    @Override
    public BFCommandNode[] getChildNodes() {
        final BFCommandNode[] result = new BFCommandNode[this.children.length];
        System.arraycopy(this.children, 0, result, 0, this.children.length);
        return result;
    }

    public BFRootNode(final BrainfuckLanguage language,
                      final FrameDescriptor descriptor,
                      final SourceSection sourceSection) {
        super(language, descriptor);
        this.sourceSection = sourceSection;
    }

    @Override
    public Object execute(final VirtualFrame frame) {
        assert lookupContextReference(BrainfuckLanguage.class).get() != null;

        initializeMemorySlots(frame);

        for (BFCommandNode command : children) {
            command.execute(frame);
        }
        return BFNull.SINGLETON;
    }

    private void initializeMemorySlots(final VirtualFrame frame) {
        final FrameDescriptor descriptor = frame.getFrameDescriptor();
        for (Object identifier : descriptor.getIdentifiers()) {
            final FrameSlot slot = descriptor.findFrameSlot(identifier);

            switch (descriptor.getFrameSlotKind(slot)) {
                case Byte:
                    frame.setByte(slot, (byte) 0);
                    break;
                case Int:
                    frame.setInt(slot, 0);
                    break;
                default:
                    logUnexpectedSlot(identifier);
            }
        }
    }

    @TruffleBoundary
    private void logUnexpectedSlot(final Object identifier) {
        System.err.printf("Found unexpected slot %s in frame descriptor. It will remain uninitialized.%n", identifier);
    }

    @Override
    public String toString() {
        return "ROOT";
    }
}
