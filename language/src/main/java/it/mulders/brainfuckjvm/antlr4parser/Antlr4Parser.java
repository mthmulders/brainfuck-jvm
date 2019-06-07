package it.mulders.brainfuckjvm.antlr4parser;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.source.Source;
import it.mulders.brainfuckjvm.BrainfuckLanguage;
import lombok.AllArgsConstructor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

@AllArgsConstructor
public class Antlr4Parser {
    /** The {@link BrainfuckLanguage} instance that generated call targets will be associated with. */
    private final BrainfuckLanguage language;

    public CallTarget parseSource(final BrainfuckLanguage language, final Source source) {
        BrainfuckLexer lexer = new BrainfuckLexer(CharStreams.fromString(source.getCharacters().toString()));

        BrainfuckParser parser = new BrainfuckParser(new CommonTokenStream(lexer));
        lexer.removeErrorListeners();
        parser.removeErrorListeners();

        BailoutErrorListener listener = new BailoutErrorListener(source);
        lexer.addErrorListener(listener);
        parser.addErrorListener(listener);
//        parser.factory = new SLNodeFactory(language, source);
//        parser.source = source;
//        parser.simplelanguage();
//        return parser.factory.getAllFunctions();
        return parser.program();
    }
}
