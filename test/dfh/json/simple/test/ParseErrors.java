package dfh.json.simple.test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import dfh.json.simple.Converter;
import dfh.json.simple.JSONSimpleException;

public class ParseErrors {

	@Test
	public void numberTest1() {
		double foo = 1.0;
		String s = "{\"a\":" + foo;
		try {
			Converter.convert(s);
			fail("should have thrown error");
		} catch (JSONSimpleException e) {
			assertTrue(e.getMessage().startsWith(
					"parsing failed at offset " + s.length()));
		}
	}

	@Test
	public void numberTest2() {
		double foo = 1.0;
		String s = "\"a\":" + foo + "}";
		try {
			Converter.convert(s);
			fail("should have thrown error");
		} catch (JSONSimpleException e) {
			assertTrue(e.getMessage().startsWith("parsing failed at offset 0"));
		}
	}

	@Test
	public void numberTest3() {
		double foo = 1.0;
		String s = "{\"a\"" + foo + "}";
		try {
			Converter.convert(s);
			fail("should have thrown error");
		} catch (JSONSimpleException e) {
			assertTrue(e.getMessage().startsWith("parsing failed at offset 4"));
		}
	}
}
