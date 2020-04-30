package it.mulders.brainfuckjvm.demoapp;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import static org.assertj.core.api.InstanceOfAssertFactories.type;

class IndexControllerTest implements WithAssertions {
    private final IndexController controller = new IndexController();

    @Test
    void should_give_example_input_program() {
        final ModelAndView result = controller.index();

        assertThat(result.getModel())
                .containsKey("input")
                .extracting(m -> m.get("input"), type(ExecutionInput.class))
                .extracting(ExecutionInput::getSource, STRING)
                .isNotEmpty();
    }
}