grammar Brainfuck;

@parser::header
{
// GENERATED CODE - DO NOT MODIFY

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.RootCallTarget;

import it.mulders.brainfuckjvm.simpleparser.parser.BFParseError;
}

@lexer::header
{
// GENERATED CODE - DO NOT MODIFY
}

@parser::members
{
private BFNodeFactory factory;
private Source source;

private static final class BailoutErrorListener extends BaseErrorListener {
    private final Source source;
    BailoutErrorListener(Source source) {
        this.source = source;
    }
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        throwParseError(source, line, charPositionInLine, (Token) offendingSymbol, msg);
    }
}

private static void throwParseError(Source source, int line, int charPositionInLine, Token token, String message) {
    int col = charPositionInLine + 1;
    String location = "-- line " + line + " col " + col + ": ";
    int length = token == null ? 1 : Math.max(token.getStopIndex() - token.getStartIndex(), 0);
    throw new BFParseError(source, line, col, length, String.format("Error(s) parsing script:%n" + location + message));
}
}

/*
 * Lexer Rules
 */

WHITESPACE             : [ \t\r\n\u000C]+  -> skip ;
COMMENT                : [A-Z] | [a-z] | '_' | '$' | [0-9] ;
DECREMENT_BYTE         : '-' { System.out.println("Hurray, a DECREMENT");
};
DECREMENT_DATA_POINTER : '<' ;
INCREMENT_BYTE         : '+' ;
INCREMENT_DATA_POINTER : '>' ;
INPUT_BYTE             : ',' ;
JUMP_BACKWARD          : ']' ;
JUMP_FORWARD           : '[' ;
OUTPUT_BYTE            : '.' ;

COMMAND                : (
    DECREMENT_BYTE |
    DECREMENT_DATA_POINTER |
    INCREMENT_BYTE |
    INCREMENT_DATA_POINTER |
    INPUT_BYTE |
    JUMP_BACKWARD |
    JUMP_FORWARD |
    OUTPUT_BYTE
) ;

/**
 * Parser rules
 */
commands: (
              DECREMENT_BYTE |
              DECREMENT_DATA_POINTER |
              INCREMENT_BYTE |
              INCREMENT_DATA_POINTER |
              INPUT_BYTE |
              JUMP_BACKWARD |
              JUMP_FORWARD |
              OUTPUT_BYTE
          )*;

program : commands EOF ;