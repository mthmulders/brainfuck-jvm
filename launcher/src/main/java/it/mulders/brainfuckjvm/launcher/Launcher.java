package it.mulders.brainfuckjvm.launcher;

import org.graalvm.launcher.AbstractLanguageLauncher;
import org.graalvm.options.OptionCategory;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Command-line launcher for running Brainfuck programs from source.
 */
public class Launcher extends AbstractLanguageLauncher {
    private static final String BRAINFUCK_ID = "bf";

    private String file = null;

    public static void main(final String... args) {
        new Launcher().launch(args);
    }

    @Override
    protected String getLanguageId() {
        return BRAINFUCK_ID;
    }

    @Override
    protected List<String> preprocessArguments(final List<String> arguments, final Map<String, String> polyglotOptions) {
        final List<String> remainder = new ArrayList<>(arguments);
        for (final String arg : arguments) {
            if (isOption(arg)) {
                polyglotOptions.putAll(parseOption(arg));
            } else {
                if (file != null) {
                    // Usually, you would accept multiple source files, but for Brainfuck that doesn't make sense.
                    System.err.println("Source file to execute is already specified, overriding it");
                }
                file = arg;
            }
            remainder.remove(arg);
        }
        return remainder;
    }

    @Override
    protected void launch(final Context.Builder contextBuilder) {
        try (final Context context = contextBuilder.build()) {
            final Source source = Source.newBuilder(BRAINFUCK_ID, new File(file)).build();
            context.eval(source);
        } catch (final IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    protected void printHelp(final OptionCategory maxCategory) {
    }

    protected static boolean isOption(final String arg) {
        return arg.length() > 2 && arg.startsWith("--");
    }

    protected static Map<String, String> parseOption(final String arg) {
        if (hasValue(arg)) {
            return parseOptionWithValue(arg);
        } else {
            return parseOptionWithoutValue(arg);
        }
    }

    protected static boolean hasValue(final String arg) {
        return arg.indexOf('=') > 0;
    }

    protected static Map<String, String> parseOptionWithoutValue(final String arg) {
        return Collections.singletonMap(arg.substring(2), "true");
    }

    protected static Map<String, String> parseOptionWithValue(final String arg) {
        final int eqIdx = arg.indexOf('=');
        return Collections.singletonMap(arg.substring(2, eqIdx), arg.substring(eqIdx + 1));
    }
}