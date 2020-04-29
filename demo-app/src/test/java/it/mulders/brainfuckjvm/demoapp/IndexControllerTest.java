package it.mulders.brainfuckjvm.demoapp;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

class IndexControllerTest implements WithAssertions {
    private static final InstanceOfAssertFactory<ExecutionInput, ObjectAssert<ExecutionInput>> EXECUTION_INPUT
            = new InstanceOfAssertFactory<>(ExecutionInput.class, Assertions::assertThat);

    private final IndexController controller = new IndexController();

    @Test
    void should_give_example_input_program() {
        final ModelAndView result = controller.index();

        assertThat(result.getModel())
                .containsKey("input")
                .extracting(m -> m.get("input")).asInstanceOf(EXECUTION_INPUT)
                .extracting(m -> m.getSource())
                .isNotEmpty();
    }
}