package it.mulders.brainfuckjvm;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

class BrainfuckFileDetectorTest implements WithAssertions {
    private BrainfuckFileDetector detector = new BrainfuckFileDetector();

    @Test
    void detects_files_ending_dot_bf() {
        final Path input = Paths.get("whatever", "foo.bf");
        assertThat(detector.probeContentType(input)).isEqualTo(BrainfuckLanguage.MIME_TYPE);
    }

    @Test
    void does_not_detect_files_ending_bf() {
        final Path input = Paths.get("whatever", "foo-bf");
        assertThat(detector.probeContentType(input)).isNull();
    }

    @Test
    void does_not_detect_files_ending_something_else() {
        final Path input = Paths.get("whatever", "foo.cs");
        assertThat(detector.probeContentType(input)).isNull();
    }
}