package dfh.json.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import dfh.grammar.Grammar;
import dfh.grammar.Match;
import dfh.grammar.MatchTest;
import dfh.grammar.Matcher;
import dfh.grammar.Options;
import dfh.grammar.util.Dotify;

/**
 * Converts between JSON and collections.
 * <p>
 * <b>Creation date:</b> Oct 5, 2011
 * 
 * @author David Houghton
 * 
 */
public class Converter {
	/**
	 * BNF grammar for JSON objects
	 */
	public static final String[] JSON_RULES = {
			//
			"   ROOT = <s> [{norm} <obj> | <array> ] <s>",//
			"    obj = '{' <s> [ <string> <s> ':' <s> <value> [ <s>  ',' <s> <string> <s> ':' <s> <value> <s> ]* <s> ]? '}'",//
			"  array = '[' <s> [ <value> [ <s> ',' <s> <value> ]* <s> ]? ']'",//
			"  value = <string> | <obj> | <boolean> | <null> | <array> | <number>",//
			" string = '\"' [ '\\\\' <sc> | <nsc> ]*+ '\"'",//
			"boolean = 'true' | 'false'",//
			"     sc = <ec> | <ue>",//
			"     ec = /[rtfbn\\\\\\/\"]/",//
			"    nsc = /[^\\\\\\p{Cc}\"]/",//
			"   null = 'null'",//
			" number = <int> | <double>",//
			"    int = /-?+(?:0|[1-9]\\d*+)/",//
			" double = /-?+(?:0|[1-9]\\d*+)(\\.\\d++)?+([eE][+-]?+\\d++)?+/",//
			"      s = /\\s*+/",//
			"     ue = /u[0-9a-fA-F]{4}/",//
	};
	/**
	 * {@link Grammar} generated from {@link #JSON_RULES}
	 */
	public static Grammar g = new Grammar(JSON_RULES);

	private Converter() {
	}

	/**
	 * Convert Java {@link Collection} object to JSON string representing an
	 * object.
	 * 
	 * @param map
	 *            {@link Map} from strings to objects
	 * @return JSON string
	 * @throws JSONSimpleException
	 */
	public static String convert(Map<String, Object> map)
			throws JSONSimpleException {
		StringBuilder b = new StringBuilder();
		convert(map, b);
		return b.toString();
	}

	/**
	 * Convert Java {@link Collection} object to JSON string representing a
	 * list.
	 * 
	 * @param collection
	 *            {@link Collection} of objects
	 * @return JSON string
	 * @throws JSONSimpleException
	 */
	public static <K extends Collection<?>> String convert(K collection)
			throws JSONSimpleException {
		StringBuilder b = new StringBuilder();
		convert(new ArrayList<Object>(collection), b);
		return b.toString();
	}

	private static final MatchTest normTest = new MatchTest() {
		@Override
		public boolean test(Match m) {
			return m.hasLabel("norm");
		}
	};

	/**
	 * Converts a JSON string to either a map from strings to objects or a list
	 * of objects.
	 * 
	 * @param json
	 *            string to parse
	 * @return Java collection, either a map or a list
	 * @throws JSONSimpleException
	 */
	public static Object convert(String json) throws JSONSimpleException {
		Matcher m = g.matches(json,
				new Options().study(false).keepRightmost(true));
		Match n = m.match();
		if (n == null) {
			n = m.rightmostMatch();
			if (n == null)
				throw new JSONSimpleException("failed to parse \"" + json
						+ "\" as JSON");
			int start = Math.max(0, n.end() - 20), end = Math.min(
					json.length(), n.end() + 20);
			StringBuilder b = new StringBuilder("parsing failed at offset ");
			b.append(n.end()).append(" marked by '<HERE>': ");
			if (start > 0)
				b.append("...");
			b.append(json.substring(start, n.end()));
			b.append("<HERE>");
			b.append(json.substring(n.end(), end));
			if (end < json.length())
				b.append("...");
			throw new JSONSimpleException(b.toString());
		}

		n = n.choose(normTest);
		for (Match child : n.children()) {
			if (child.hasLabel("array"))
				return convertArray(child, json);
			else if (child.hasLabel("obj"))
				return convertObject(child, json);
		}
		throw new JSONSimpleException(
				"LOGIC ERROR: parsable string failed to parse to either object or array");
	}

	/**
	 * Cleans out unnecessary whitespace from JSON string.
	 * 
	 * @param json
	 * @return json with unnecessary whitespace removed
	 * @throws JSONSimpleException
	 */
	@SuppressWarnings("unchecked")
	public static String compress(String json) throws JSONSimpleException {
		Object obj = convert(json);
		if (obj instanceof Map<?, ?>)
			return convert((Map<String, Object>) obj);
		return convert((List<Object>) obj);
	}

	private static final MatchTest stringOrValue = new MatchTest() {
		@Override
		public boolean test(Match m) {
			return m.hasLabel("string") || m.hasLabel("value");
		}
	};

	private static Map<String, Object> convertObject(final Match m, String json) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		String key = null;
		for (Match child : m.getClosestDescendants(stringOrValue)) {
			if (child.hasLabel("string"))
				key = convertString(child, json);
			else {
				Object o = convertValue(child, json);
				map.put(key, o);
			}
		}
		return map;
	}

	private static Object convertValue(Match m, String json) {
		Match child = m.children()[0];
		if (child.hasLabel("string")) {
			return convertString(child, json);
		} else if (child.hasLabel("number")) {
			return convertNumber(child);
		} else if (child.hasLabel("obj")) {
			return convertObject(child, json);
		} else if (child.hasLabel("boolean")) {
			return convertBoolean(child);
		} else if (child.hasLabel("array")) {
			return convertArray(child, json);
		} else
			return null;
	}

	private static final MatchTest valueTest = new MatchTest() {
		@Override
		public boolean test(Match o) {
			return o.hasLabel("value");
		}
	};

	private static Object convertArray(Match m, String json) {
		List<Object> list = new ArrayList<Object>();
		for (Match child : m.getClosestDescendants(valueTest)) {
			list.add(convertValue(child, json));
		}
		return list;
	}

	private static Object convertBoolean(Match child) {
		return new Boolean(child.group());
	}

	private static Object convertNumber(Match child) {
		child = child.children()[0];
		if (child.hasLabel("int"))
			return new Integer(child.group());
		else
			return new Double(child.group());
	}

	private static final MatchTest scOrNsc = new MatchTest() {
		@Override
		public boolean test(Match o) {
			return o.hasLabel("sc") || o.hasLabel("nsc");
		}
	};

	private static String convertString(Match m, String json) {
		StringBuilder b = new StringBuilder();
		for (Match child : m.get(scOrNsc)) {
			if (child.hasLabel("sc"))
				convertSpecialCharacter(child, json, b);
			else
				convertOrdinaryCharacter(child, json, b);
		}
		return b.toString();
	}

	private static void convertOrdinaryCharacter(Match m, String json,
			StringBuilder b) {
		b.append(json.substring(m.start(), m.end()));
	}

	private static void convertSpecialCharacter(Match m, String json,
			StringBuilder b) {
		Match child = m.children()[0];
		String s = json.substring(child.start(), child.end());
		if (child.hasLabel("ue")) {
			char c = (char) Integer.parseInt(s.substring(1), 16);
			b.append(c);
		} else {
			switch (s.charAt(0)) {
			case 'r':
				b.append('\r');
				break;
			case 't':
				b.append('\t');
				break;
			case 'b':
				b.append('\b');
				break;
			case 'n':
				b.append('\n');
				break;
			case 'f':
				b.append('\f');
				break;
			case '\\':
				b.append('\\');
				break;
			case '"':
				b.append('"');
				break;
			case '/':
				b.append('/');
				break;
			default:
				throw new RuntimeException(
						"grammar out of sync with conversion code; unexpected escaped character "
								+ s);
			}
		}
	}

	private static void convert(Map<String, Object> map, StringBuilder b)
			throws JSONSimpleException {
		b.append('{');
		boolean nonInitial = false;
		for (Entry<String, Object> e : map.entrySet()) {
			if (nonInitial)
				b.append(',');
			else
				nonInitial = true;
			convert(e.getKey(), b);
			b.append(':');
			convert(e.getValue(), b);
		}
		b.append('}');
	}

	private static void convert(String key, StringBuilder b) {
		b.append('"');
		escape(key, b);
		b.append('"');
	}

	private static void escape(String s, StringBuilder b) {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '\n':
				b.append("\\n");
				break;
			case '\r':
				b.append("\\r");
				break;
			case '\t':
				b.append("\\t");
				break;
			case '\f':
				b.append("\\f");
				break;
			case '\b':
				b.append("\\b");
				break;
			case '\\':
				b.append("\\\\");
				break;
			case '/':
				b.append("\\/");
				break;
			case '"':
				b.append("\\\"");
				break;
			default:
				b.append(c);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void convert(Object value, StringBuilder b)
			throws JSONSimpleException {
		if (value == null)
			b.append("null");
		else if (value instanceof String)
			convert((String) value, b);
		else if (value instanceof Number)
			convert((Number) value, b);
		else if (value instanceof Object[])
			convert((Object[]) value, b);
		else if (value instanceof List<?>)
			convert((List<Object>) value, b);
		else if (value instanceof Map<?, ?>)
			convert((Map<String, Object>) value, b);
		else if (value instanceof Boolean)
			b.append(value.toString());
		else
			throw new JSONSimpleException(
					"values must be strings, arrays, lists, maps, null, booleans, or numbers");
	}

	private static void convert(Number n, StringBuilder b) {
		b.append(n.toString());
	}

	private static void convert(Object[] array, StringBuilder b)
			throws JSONSimpleException {
		b.append('[');
		boolean nonInitial = false;
		for (Object o : array) {
			if (nonInitial)
				b.append(',');
			else
				nonInitial = true;
			convert(o, b);
		}
		b.append(']');
	}

	private static void convert(List<Object> list, StringBuilder b)
			throws JSONSimpleException {
		b.append('[');
		boolean nonInitial = false;
		for (Object o : list) {
			if (nonInitial)
				b.append(',');
			else
				nonInitial = true;
			convert(o, b);
		}
		b.append(']');
	}
}
