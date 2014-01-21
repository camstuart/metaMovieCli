package au.net.asoftware.metamovie;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class MovieTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Setting up tests");
	}

	@Test
	public void test() {
		assertTrue(1 == 1);
	}

	@Test
	public void anotherTest() {
		assertTrue(1 == 1);
	}

}
