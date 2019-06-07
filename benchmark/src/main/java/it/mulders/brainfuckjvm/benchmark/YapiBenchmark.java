package it.mulders.brainfuckjvm.benchmark;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.util.NullOutputStream;

import java.io.IOException;

public class YapiBenchmark {
    @State(Scope.Benchmark)
    public static class Input {
        final String source = "[ yet another pi calculation program in bf\n" +
                "\n" +
                "  Just like for pi16.b the accuracy of the result depends on the cellsize:\n" +
                "\n" +
                "   - using  8 bit cells causes an overflow after 4 digits\n" +
                "   - using 16 bit cells causes an overflow after 537 digits\n" +
                "   - using 32 bit cells causes an overflow after several millions of digits\n" +
                "\n" +
                "  It's about ~38 times shorter than pi16.b, ~364 times faster and works with\n" +
                "  not-wrapping (bignum) implementations.\n" +
                "\n" +
                "  by Felix Nawothnig (felix.nawothnig@t-online.de) ]\n" +
                "\n" +
                ">  +++++ +++++ +++++ (15 digits)\n" +
                "\n" +
                "[<+>>>>>>>>++++++++++<<<<<<<-]>+++++[<+++++++++>-]+>>>>>>+[<<+++[>>[-<]<[>]<-]>>\n" +
                "[>+>]<[<]>]>[[->>>>+<<<<]>>>+++>-]<[<<<<]<<<<<<<<+[->>>>>>>>>>>>[<+[->>>>+<<<<]>\n" +
                ">>>>]<<<<[>>>>>[<<<<+>>>>-]<<<<<-[<<++++++++++>>-]>>>[<<[<+<<+>>>-]<[>+<-]<++<<+\n" +
                ">>>>>>-]<<[-]<<-<[->>+<-[>>>]>[[<+>-]>+>>]<<<<<]>[-]>+<<<-[>>+<<-]<]<<<<+>>>>>>>\n" +
                ">[-]>[<<<+>>>-]<<++++++++++<[->>+<-[>>>]>[[<+>-]>+>>]<<<<<]>[-]>+>[<<+<+>>>-]<<<\n" +
                "<+<+>>[-[-[-[-[-[-[-[-[-<->[-<+<->>]]]]]]]]]]<[+++++[<<<++++++++<++++++++>>>>-]<\n" +
                "<<<+<->>>>[>+<<<+++++++++<->>>-]<<<<<[>>+<<-]+<[->-<]>[>>.<<<<[+.[-]]>>-]>[>>.<<\n" +
                "-]>[-]>[-]>>>[>>[<<<<<<<<+>>>>>>>>-]<<-]]>>[-]<<<[-]<<<<<<<<]++++++++++.\n";
    }


    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void executeYapi(Input input) throws IOException {
        final Source source = Source.newBuilder("bf", input.source, "user input").build();
        final Context context = Context.newBuilder("bf").out(new NullOutputStream()).build();
        context.eval(source);
    }

}