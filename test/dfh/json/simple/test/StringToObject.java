package dfh.json.simple.test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import dfh.json.simple.Converter;
import dfh.json.simple.JSONSimpleException;

public class StringToObject {

	@Test
	public void numberTest1() {
		try {
			double foo = 1.0;
			Map<String, Object> map = Converter.convert("{\"a\":" + foo + "}");
			Double d = (Double) map.get("a");
			assertTrue("", d.doubleValue() == foo);
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void numberTestE() {
		try {
			double foo = 1e14;
			Map<String, Object> map = Converter.convert("{\"a\":" + foo + "}");
			Double d = (Double) map.get("a");
			assertTrue("", d.doubleValue() == foo);
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void unicodeTest() {
		try {
			Map<String, Object> map = Converter.convert("{\"a\":\"\\u0950\"}");
			String s = (String) map.get("a");
			assertTrue("", s.charAt(0) == 'ॐ');
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void escapeTest() {
		try {
			Map<String, Object> map = Converter
					.convert("{\"a\":\"\\r\\n\\t\\b\\f\\\\\\/\\\"\"}");
			String s = (String) map.get("a");
			assertEquals("got all escapes", s, "\r\n\t\b\f\\/\"");
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void nullTest() {
		try {
			Map<String, Object> map = Converter.convert("{\"a\":null}");
			Object o = map.get("a");
			assertNull("'null' translates to null", o);
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void booleanTrueTest() {
		try {
			Map<String, Object> map = Converter.convert("{\"a\":true}");
			Boolean b = (Boolean) map.get("a");
			assertTrue("'true' translates to true", b);
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void booleanFalseTest() {
		try {
			Map<String, Object> map = Converter.convert("{\"a\":false}");
			Boolean b = (Boolean) map.get("a");
			assertTrue("'false' translates to false", !b);
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void listTest1() {
		try {
			Map<String, Object> map = Converter.convert("{\"a\":[]}");
			@SuppressWarnings("unchecked")
			List<Object> l = (List<Object>) map.get("a");
			assertTrue("list is empty", l.isEmpty());
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void listTest2() {
		try {
			Map<String, Object> map = Converter
					.convert("{\"a\":[1,null,false,{}]}");
			@SuppressWarnings("unchecked")
			List<Object> l = (List<Object>) map.get("a");
			assertTrue("first entity is 1", 1 == ((Number) l.get(0)).intValue());
			assertTrue("second entity is null", l.get(1) == null);
			assertTrue("third entity is false", !((Boolean) l.get(2)));
			assertTrue("fourth entity is a map", l.get(3) instanceof Map<?, ?>);
			assertTrue("only four entities", l.size() == 4);
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void mapTest1() {
		try {
			Map<String, Object> map = Converter.convert("{\"a\":{}}");
			@SuppressWarnings("unchecked")
			Map<String, Object> m = (Map<String, Object>) map.get("a");
			assertTrue("map is empty", m.isEmpty());
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void mapTest2() {
		try {
			Map<String, Object> map = Converter
					.convert("{\"a\":{\"foo\":null,\"bar\":false}}");
			@SuppressWarnings("unchecked")
			Map<String, Object> m = (Map<String, Object>) map.get("a");
			assertTrue("map has two mappings", m.size() == 2);
			assertTrue("foo is null", m.get("foo") == null);
			assertTrue("bar is false", !((Boolean) m.get("bar")));
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}
}
