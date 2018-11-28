package it.mulders.brainfuckjvm;

import com.oracle.truffle.api.TruffleLanguage;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

@Getter
public class BrainfuckContext {
    private final TruffleLanguage.Env env;
    private final BrainfuckLanguage language;
    private final BufferedReader input;
    private final PrintWriter output;

    public BrainfuckContext(final BrainfuckLanguage language, final TruffleLanguage.Env env) {
        this.env = env;
        this.language = language;
        this.input = new BufferedReader(new InputStreamReader(env.in()));
        this.output = new PrintWriter(env.out(), true);
    }
}
