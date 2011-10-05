package dfh.json.simple.test;

import java.util.Map;
import java.util.TreeMap;

import dfh.json.simple.Converter;
import dfh.json.simple.JSONSimpleException;

/**
 * For eyeballing what we've got so far.
 * <p>
 * <b>Creation date:</b> Oct 5, 2011
 * 
 * @author David Houghton
 * 
 */
public class Eyeball {

	/**
	 * Jsonifies a map.
	 * 
	 * @param args
	 *            no args
	 * @throws JSONSimpleException
	 */
	public static void main(String[] args) throws JSONSimpleException {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("key", 1);
		map.put("foo", false);
		map.put("bar", null);
		map.put("baz", new Object[] { 1, 3.5, 1e14 });
		Map<String, Object> subMap = new TreeMap<String, Object>();
		map.put("quux", subMap);
		subMap.put("A", new Object[] { "foo", null, 3 });
		subMap.put("B", "");
		subMap.put("C", "\n\t\f\b\\/\r£¶Æβ\"");
		System.out.println(Converter.convert(map));
	}

}
