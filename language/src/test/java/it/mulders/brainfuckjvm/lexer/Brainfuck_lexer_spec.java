//package it.mulders.brainfuckjvm.lexer;
//
//import it.mulders.brainfuckjvm.BrainfuckLanguage;
//import org.graalvm.polyglot.Source;
//
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//
//import static org.hamcrest.CoreMatchers.hasItem;
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.collection.IsEmptyCollection.empty;
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//
//class Brainfuck_lexer_spec {
//    private BrainfuckLexer lexer = new BrainfuckLexer();
//
//    private Source createSource(final String input) {
//        return Source.create(BrainfuckLanguage.ID, input);
//    }
//
//    @Test
//    void should_recognise_increment_the_data_pointer_command() {
//        assertThat(lexer.parse(createSource(">")), hasItem(BrainfuckToken.INCREMENT_DATA_POINTER));
//    }
//
//    @Test
//    void should_recognise_decrement_the_data_pointer_command() {
//        assertThat(lexer.parse("<"), hasItem(BrainfuckToken.DECREMENT_DATA_POINTER));
//    }
//
//    @Test
//    void should_recognise_increment_byte_command() {
//        assertThat(lexer.parse("+"), hasItem(BrainfuckToken.INCREMENT_BYTE));
//    }
//
//    @Test
//    void should_recognise_decrement_byte_command() {
//        assertThat(lexer.parse("-"), hasItem(BrainfuckToken.DECREMENT_BYTE));
//    }
//
//    @Test
//    void should_recognise_output_byte_command() {
//        assertThat(lexer.parse("."), hasItem(BrainfuckToken.OUTPUT_BYTE));
//    }
//
//    @Test
//    void should_recognise_input_byte_command() {
//        assertThat(lexer.parse(","), hasItem(BrainfuckToken.INPUT_BYTE));
//    }
//
//    @Test
//    void should_recognise_jump_forward_command() {
//        assertThat(lexer.parse("["), hasItem(BrainfuckToken.JUMP_FORWARD));
//    }
//
//    @Test
//    void should_recognise_jump_backward_command() {
//        assertThat(lexer.parse("]"), hasItem(BrainfuckToken.JUMP_BACKWARD));
//    }
//
//    @Nested
//    class with_other_input {
//        @Test
//        void should_not_throw_exception() {
//            assertDoesNotThrow(() -> lexer.parse("|"));
//        }
//
//        @Test
//        void should_return_empty_tokens() {
//            assertThat(lexer.parse("|"), is(empty()));
//        }
//    }
//}