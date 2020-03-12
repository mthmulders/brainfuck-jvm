package it.mulders.brainfuckjvm.demoapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class DemoApplicationIT {
	@Autowired
	private BrainfuckEngine engine;

	@Test
	public void contextLoads() {
		assertThat(engine).isNotNull();
	}

}
