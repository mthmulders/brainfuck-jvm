package it.mulders.brainfuckjvm;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.TruffleRuntime;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;
import it.mulders.brainfuckjvm.ast.BFRootNode;
import it.mulders.brainfuckjvm.lexer.BrainfuckLexer;
import it.mulders.brainfuckjvm.lexer.BrainfuckToken;
import it.mulders.brainfuckjvm.parser.BrainfuckParser;

import java.util.stream.Stream;

@TruffleLanguage.Registration(
        id = BrainfuckLanguage.ID,
        name = "SL",
        defaultMimeType = BrainfuckLanguage.MIME_TYPE,
        characterMimeTypes = BrainfuckLanguage.MIME_TYPE,
        contextPolicy = TruffleLanguage.ContextPolicy.SHARED
)
public final class BrainfuckLanguage extends TruffleLanguage<BrainfuckContext> {
    public static final String ID = "bf";
    public static final String MIME_TYPE = "application/x-bf";

    private BrainfuckLexer lexer = new BrainfuckLexer();
    private BrainfuckParser parser = new BrainfuckParser(this);

    @Override
    protected BrainfuckContext createContext(final Env env) {
        return new BrainfuckContext(this, env);
    }

    @Override
    protected boolean isObjectOfLanguage(final Object object) {
        // might need a check on Brainfuck objects here, like for the Simple Language.
        return object instanceof TruffleObject;
    }

    @Override
    protected CallTarget parse(final ParsingRequest request) throws Exception {
        final Source source = request.getSource();

        final Stream<BrainfuckToken> tokens = lexer.parse(source);

        final BFRootNode rootNode = parser.parse(source, tokens);

        final TruffleRuntime runtime = Truffle.getRuntime();

        return runtime.createCallTarget(rootNode);
    }
}