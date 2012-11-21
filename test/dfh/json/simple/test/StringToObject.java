package dfh.json.simple.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
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
			Object o = Converter.convert("{\"a\":" + foo + "}");
			assertTrue("parsed to map", o instanceof Map<?, ?>);
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) o;
			BigDecimal d = (BigDecimal) map.get("a");
			assertTrue(d.doubleValue() == foo);
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void numberTestE() {
		try {
			double foo = 1e14;
			Object o = Converter.convert("{\"a\":" + foo + "}");
			assertTrue("parsed to map", o instanceof Map<?, ?>);
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) o;
			BigDecimal d = (BigDecimal) map.get("a");
			assertTrue(d.doubleValue() == foo);
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void numberTestBigInteger() {
		try {
			BigInteger bi = new BigInteger("1234567890");
			Object o = Converter.convert("{\"a\":" + bi + "}");
			assertTrue("parsed to map", o instanceof Map<?, ?>);
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) o;
			BigInteger d = (BigInteger) map.get("a");
			assertEquals(bi, d);
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void unicodeTest() {
		try {
			Object o = Converter.convert("{\"a\":\"\\u0950\"}");
			assertTrue("parsed to map", o instanceof Map<?, ?>);
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) o;
			String s = (String) map.get("a");
			assertTrue("", s.charAt(0) == 'ॐ');
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void escapeTest() {
		try {
			Object o = Converter
					.convert("{\"a\":\"\\r\\n\\t\\b\\f\\\\\\/\\\"\"}");
			assertTrue("parsed to map", o instanceof Map<?, ?>);
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) o;
			String s = (String) map.get("a");
			assertEquals("got all escapes", s, "\r\n\t\b\f\\/\"");
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void nullTest() {
		try {
			Object o = Converter.convert("{\"a\":null}");
			assertTrue("parsed to map", o instanceof Map<?, ?>);
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) o;
			o = map.get("a");
			assertNull("'null' translates to null", o);
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void booleanTrueTest() {
		try {
			Object o = Converter.convert("{\"a\":true}");
			assertTrue("parsed to map", o instanceof Map<?, ?>);
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) o;
			Boolean b = (Boolean) map.get("a");
			assertTrue("'true' translates to true", b);
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void booleanFalseTest() {
		try {
			Object o = Converter.convert("{\"a\":false}");
			assertTrue("parsed to map", o instanceof Map<?, ?>);
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) o;
			Boolean b = (Boolean) map.get("a");
			assertTrue("'false' translates to false", !b);
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void listTest1() {
		try {
			Object o = Converter.convert("{\"a\":[]}");
			assertTrue("parsed to map", o instanceof Map<?, ?>);
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) o;
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
			Object o = Converter.convert("{\"a\":[1,null,false,{}]}");
			assertTrue("parsed to map", o instanceof Map<?, ?>);
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) o;
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
			Object o = Converter.convert("{\"a\":{}}");
			assertTrue("parsed to map", o instanceof Map<?, ?>);
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) o;
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
			Object o = Converter
					.convert("{\"a\":{\"foo\":null,\"bar\":false}}");
			assertTrue("parsed to map", o instanceof Map<?, ?>);
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) o;
			@SuppressWarnings("unchecked")
			Map<String, Object> m = (Map<String, Object>) map.get("a");
			assertTrue("map has two mappings", m.size() == 2);
			assertTrue("foo is null", m.get("foo") == null);
			assertTrue("bar is false", !((Boolean) m.get("bar")));
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void arrayTest1() {
		try {
			Object o = Converter.convert("[{\"foo\":null},{\"bar\":false}]");
			assertTrue("parsed to array", o instanceof List<?>);
			List<Object> list = (List<Object>) o;
			assertTrue("array has length 2", list.size() == 2);
			Map<String, Object> m = (Map<String, Object>) list.get(0);
			assertTrue("map has one mappings", m.size() == 1);
			assertTrue("foo is null", m.get("foo") == null);
			m = (Map<String, Object>) list.get(1);
			assertTrue("map has one mappings", m.size() == 1);
			assertTrue("bar is false", !((Boolean) m.get("bar")));
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void arrayTest2() {
		try {
			Object o = Converter.convert("[]");
			assertTrue("parsed to array", o instanceof List<?>);
			@SuppressWarnings("unchecked")
			List<Object> list = (List<Object>) o;
			assertTrue("array is empty", list.isEmpty());
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

}
