package it.mulders.brainfuckjvm.demoapp;

import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import static org.assertj.core.api.InstanceOfAssertFactories.map;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RunProgramControllerTest implements WithAssertions {
    private static final InstanceOfAssertFactory<ExecutionResult, ExecutionResultAssert> EXECUTION_RESULT = new InstanceOfAssertFactory<>(ExecutionResult.class, ExecutionResultAssert::assertThat);

    private final BrainfuckEngine brainfuckEngine = mock(BrainfuckEngine.class);

    private final RunProgramController controller = new RunProgramController(brainfuckEngine);

    @Test
    void should_run_program() {
        final ExecutionInput input = new ExecutionInput();
        input.setSource("");
        final ExecutionResult output = ExecutionResult.builder().output("Hello, world").build();
        when(brainfuckEngine.runProgram(anyString())).thenReturn(output);

        final ModelAndView result = controller.runProgram(input);

        assertThat(result)
                .extracting(ModelAndView::getModel, map(String.class, Object.class))
                .containsKey("result")
                .extracting(m -> m.get("result"), as(EXECUTION_RESULT))
                .hasOutput("Hello, world");
    }
}