package it.mulders.brainfuckjvm.demoapp;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class DemoApplicationIT implements WithAssertions {
	@Autowired
	private BrainfuckEngine engine;

	@Test
	public void contextLoads() {
		assertThat(engine).isNotNull();
	}

}
