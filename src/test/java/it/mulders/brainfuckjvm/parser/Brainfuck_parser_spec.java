//package it.mulders.brainfuckjvm.parser;
//
//import com.oracle.truffle.api.nodes.Node;
//import it.mulders.brainfuckjvm.BrainfuckLanguage;
//import org.junit.jupiter.api.Test;
//
//import java.util.Collections;
//
//import static it.mulders.brainfuckjvm.lexer.BrainfuckToken.*;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.notNullValue;
//import static org.hamcrest.MatcherAssert.assertThat;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class Brainfuck_parser_spec {
//    private BrainfuckParser parser = new BrainfuckParser(new BrainfuckLanguage());
//
//    @Test
//    void valid_command_should_return_node() {
//        final Node result = parser.parse(Collections.singletonList(INCREMENT_BYTE));
//        assertThat(result, is(notNullValue()));
//    }
//}