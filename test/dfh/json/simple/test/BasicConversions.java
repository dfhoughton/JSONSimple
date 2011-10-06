package dfh.json.simple.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import dfh.json.simple.Converter;
import dfh.json.simple.JSONSimpleException;

/**
 * Makes sure we can handle all types.
 * <p>
 * <b>Creation date:</b> Oct 6, 2011
 * 
 * @author David Houghton
 * 
 */
public class BasicConversions {

	@Test
	public void nullConversion() {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("a", null);
		try {
			assertEquals("null value", "{\"a\":null}", Converter.convert(map));
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void stringConversionBasic() {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("a", "foo");
		try {
			assertEquals("null value", "{\"a\":\"foo\"}",
					Converter.convert(map));
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void stringConversionWeird() {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("a", "\n\t\f\b\\/\r");
		try {
			assertEquals("null value", "{\"a\":\"\\n\\t\\f\\b\\\\\\/\\r\"}",
					Converter.convert(map));
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void mapConversionEmpty() {
		Map<String, Object> map = new TreeMap<String, Object>(), subMap = new TreeMap<String, Object>();
		map.put("a", subMap);
		try {
			assertEquals("null value", "{\"a\":{}}", Converter.convert(map));
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void mapConversionSimple() {
		Map<String, Object> map = new TreeMap<String, Object>(), subMap = new TreeMap<String, Object>();
		map.put("a", subMap);
		subMap.put("a", null);
		try {
			assertEquals("null value", "{\"a\":{\"a\":null}}",
					Converter.convert(map));
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void integerConversion() {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("a", 1);
		try {
			assertEquals("null value", "{\"a\":1}", Converter.convert(map));
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void floatConversion() {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("a", 1.1);
		try {
			assertEquals("null value", "{\"a\":1.1}", Converter.convert(map));
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void arrayConversionEmpty() {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("a", new Object[] {});
		try {
			assertEquals("null value", "{\"a\":[]}", Converter.convert(map));
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void arrayConversionFull() {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("a", new Object[] { 1, null });
		try {
			assertEquals("null value", "{\"a\":[1,null]}",
					Converter.convert(map));
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void listConversionEmpty() {
		Map<String, Object> map = new TreeMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		map.put("a", list);
		try {
			assertEquals("null value", "{\"a\":[]}", Converter.convert(map));
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void listConversionFull() {
		Map<String, Object> map = new TreeMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		list.add(1);
		list.add(null);
		map.put("a", list);
		try {
			assertEquals("null value", "{\"a\":[1,null]}",
					Converter.convert(map));
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void booleanConversion() {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("a", true);
		try {
			assertEquals("null value", "{\"a\":true}", Converter.convert(map));
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

}
