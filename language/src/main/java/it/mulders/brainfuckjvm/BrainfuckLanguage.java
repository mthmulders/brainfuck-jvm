package it.mulders.brainfuckjvm;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.instrumentation.ProvidedTags;
import com.oracle.truffle.api.instrumentation.StandardTags;
import com.oracle.truffle.api.interop.TruffleObject;
import it.mulders.brainfuckjvm.simpleparser.SimpleParser;

@ProvidedTags({ StandardTags.StatementTag.class })
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

    private final SimpleParser simpleParser = new SimpleParser(this);

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
    protected CallTarget parse(final ParsingRequest request) {
        return simpleParser.parseSource(request.getSource());
    }
}