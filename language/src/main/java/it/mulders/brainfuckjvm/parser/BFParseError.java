package it.mulders.brainfuckjvm.parser;

import com.oracle.truffle.api.TruffleException;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;
import lombok.Getter;

@Getter
public class BFParseError extends RuntimeException implements TruffleException {
    private final Source source;
    private final int sourceCharIndex;
    private final int sourceLength;

    public BFParseError(final Source source, final int sourceCharIndex, final int sourceLength, final String message) {
        super(message);
        this.source = source;
        this.sourceCharIndex = sourceCharIndex;
        this.sourceLength = sourceLength;
    }

    @Override
    public SourceSection getSourceLocation() {
        return source.createSection(sourceCharIndex, sourceLength);
    }

    @Override
    public Node getLocation() {
        return null;
    }

    @Override
    public boolean isSyntaxError() {
        return true;
    }
}
