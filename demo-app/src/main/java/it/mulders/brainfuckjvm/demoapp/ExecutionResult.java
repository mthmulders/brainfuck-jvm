package it.mulders.brainfuckjvm.demoapp;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ExecutionResult {
    private final String errorMessage;
    private final long executionTime;
    private final String output;
}
