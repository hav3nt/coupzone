package hello;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

import org.junit.Test;

public class GreeterTest {

	
	@Test
	public void greeterSaysHello() {
		assertThat("Hello Contains Here", containsString("Hello"));
	}

}