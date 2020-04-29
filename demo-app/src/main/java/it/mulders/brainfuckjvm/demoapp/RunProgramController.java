package it.mulders.brainfuckjvm.demoapp;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@AllArgsConstructor(onConstructor = @__({ @Autowired }))
@Controller
@Slf4j
public class RunProgramController {
    private final BrainfuckEngine brainfuckEngine;

    @PostMapping("/run")
    public ModelAndView runProgram(@ModelAttribute("input") final ExecutionInput input) {
        final String source = input.getSource();

        log.info("Running Brainfuck source code ({} chars)", source.length());

        final ExecutionResult result = brainfuckEngine.runProgram(source);

        final Map<String, Object> model = new HashMap<>();
        model.put("result", result);

        return new ModelAndView("output", model);
    }
}
