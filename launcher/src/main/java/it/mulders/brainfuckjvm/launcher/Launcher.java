package it.mulders.brainfuckjvm.launcher;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Source;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Launcher {
    private static final String BRAINFUCK_ID = "bf";

    protected static final int EXIT_NO_SOURCE = 1;
    protected static final int EXIT_INTERNAL_ERROR = 2;
    protected static final int EXIT_PROGRAM_ERROR = 3;

    public static void main(final String... args) throws IOException {
        System.exit(run(args));
    }

    @SuppressWarnings("squid:S106")
    protected static int run(final String... args) throws IOException {
        String file = null;
        final Map<String, String> options = new HashMap<>();
        for (String arg : args) {
            if (isOption(arg)) {
                options.putAll(parseOption(arg));
            } else {
                if (file != null) {
                    System.err.println("Source file to execute is already specified, overriding it");
                }
                file = arg;
            }
        }

        if (file == null) {
            System.err.println("Need exactly one argument, the name of the source file to execute.");
            return EXIT_NO_SOURCE;
        }

        final File sourceFile = new File(args[0]);

        if (!verifyFileExists(sourceFile) || !verifyFileIsReadable(sourceFile)) {
            return EXIT_NO_SOURCE;
        } else {
            return executeSourceFile(sourceFile, options);
        }
    }

    protected static boolean isOption(final String arg) {
        return arg.length() > 2 && arg.startsWith("--");
    }

    @SuppressWarnings("squid:S106")
    protected static boolean verifyFileExists(final File file) {
        if (!file.canRead()) {
            System.err.println("The given source file " + file.getAbsolutePath() + " can't be read");
            return false;
        }
        return true;
    }

    @SuppressWarnings("squid:S106")
    protected static boolean verifyFileIsReadable(final File file) {
        if (!file.exists()) {
            System.err.println("The given source file " + file.getAbsolutePath() + " can't be found");
            return false;
        }
        return true;
    }

    @SuppressWarnings("squid:S106")
    private static int executeSourceFile(final File file, final Map<String, String> options) throws IOException {
        final Source source = Source.newBuilder(BRAINFUCK_ID, file).build();

        try (final Context context = Context.newBuilder(BRAINFUCK_ID).options(options).build()) {
            context.eval(source);
            return 0;
        } catch (final PolyglotException ex) {
            if (ex.isInternalError()) {
                // For internal errors we print the full stack trace to System.err
                System.err.println("An internal error occurred in the polyglot runtime, the guest language or an instrument");
                ex.printStackTrace(System.err);
                return EXIT_INTERNAL_ERROR;
            } else {
                System.err.println(ex.getMessage());
                return EXIT_PROGRAM_ERROR;
            }
        }
    }

    protected static Map<String, String> parseOption(final String arg) {
        if (hasValue(arg)) {
            return parseOptionWithValue(arg);
        } else {
            return parseOptionWithoutValue(arg);
        }

        /*
        final int eqIdx = arg.indexOf('=');
        String key;
        String value;
        if (eqIdx < 0) {
            key = arg.substring(2);
            value = null;
        } else {
            key = arg.substring(2, eqIdx);
            value = arg.substring(eqIdx + 1);
        }

        if (value == null) {
            value = "true";
        }
        final int index = key.indexOf('.');
        String group = key;
        if (index >= 0) {
            group = group.substring(0, index);
        }

        final Map<String, String> result = new HashMap<>();
        result.put(key, value);
        return result;
        */
    }

    private static boolean hasValue(final String arg) {
        return arg.indexOf('=') > 0;
    }

    private static Map<String, String> parseOptionWithoutValue(final String arg) {
        return Collections.singletonMap(arg.substring(2), "true");
    }

    private static Map<String, String> parseOptionWithValue(final String arg) {
        final int eqIdx = arg.indexOf('=');
        return Collections.singletonMap(arg.substring(2, eqIdx), arg.substring(eqIdx + 1));
    }
}