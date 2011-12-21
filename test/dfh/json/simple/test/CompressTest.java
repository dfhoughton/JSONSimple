package dfh.json.simple.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import dfh.json.simple.Converter;
import dfh.json.simple.JSONSimpleException;

public class CompressTest {

	@Test
	public void test1() {
		String s = " [\"a\" , null ]  ";
		try {
			String s2 = Converter.compress(s);
			assertTrue("", s2.equals(s.replaceAll("\\s++", "")));
		} catch (JSONSimpleException e) {
			fail("threw error: " + e.getMessage());
		}
	}

	@Test
	public void test2() {
		String s = " {\"a\" : null }  ";
		try {
			String s2 = Converter.compress(s);
			assertTrue("", s2.equals(s.replaceAll("\\s++", "")));
		} catch (JSONSimpleException e) {
			fail("threw error: " + e.getMessage());
		}
	}

	@Test
	public void test3() {
		String s = " {\"a\" : 1.0 }  ";
		try {
			String s2 = Converter.compress(s);
			assertTrue("", s2.equals(s.replaceAll("\\s++", "")));
		} catch (JSONSimpleException e) {
			fail("threw error: " + e.getMessage());
		}
	}

	@Test
	public void test4() {
		String s = " {\"a\" : 1 }  ";
		try {
			String s2 = Converter.compress(s);
			assertTrue("", s2.equals(s.replaceAll("\\s++", "")));
		} catch (JSONSimpleException e) {
			fail("threw error: " + e.getMessage());
		}
	}

}
