package it.mulders.brainfuckjvm.demoapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoAppApplicationTests {
	@Autowired
	private BrainfuckEngine engine;

	@Test
	public void contextLoads() {
		assertThat(engine, is(notNullValue()));
	}

}
