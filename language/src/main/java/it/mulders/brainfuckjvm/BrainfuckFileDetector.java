package it.mulders.brainfuckjvm;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.spi.FileTypeDetector;

public class BrainfuckFileDetector extends FileTypeDetector {
    @Override
    public String probeContentType(final Path path) throws IOException {
        final String fileName = path.getFileName().toString();
        if (fileName.endsWith(".bf") || fileName.endsWith(".b")) {
            return BrainfuckLanguage.MIME_TYPE;
        }

        return null;
    }
}
