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

	/**
	 * Default indentation size in spaces when calling {@link #pretty(String)}.
	 */
	public static final int DEFAULT_INDENTATION = 3;

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
		return convert(map, -1);
	}

	/**
	 * Convert Java {@link Collection} object to JSON string representing an
	 * object. Nested values are indented.
	 * 
	 * @param map
	 *            {@link Map} from strings to objects
	 * @param indent
	 *            amount nested values are to be indented relative to their
	 *            context; if this is less than 0, there will be no indentation
	 * @return JSON string
	 * @throws JSONSimpleException
	 */
	public static String convert(Map<String, Object> map, int indent)
			throws JSONSimpleException {
		StringBuilder b = new StringBuilder();
		convert(map, b, indent, 0);
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
		return convert(collection, -1);
	}

	/**
	 * Convert Java object array to JSON string representing a list.
	 * 
	 * @param list
	 *            array of objects
	 * @return JSON string
	 * @throws JSONSimpleException
	 */
	public static String convert(Object[] list) throws JSONSimpleException {
		return convert(list, -1);
	}

	/**
	 * Convert Java object array to JSON string representing a list, indenting
	 * nested values.
	 * 
	 * @param list
	 *            array of objects
	 * @param indent
	 *            amount to indent nested values; if this is less than 0, there
	 *            will be no indentation
	 * @return JSON string
	 * @throws JSONSimpleException
	 */
	public static String convert(Object[] list, int indent)
			throws JSONSimpleException {
		StringBuilder b = new StringBuilder();
		convert(list, b, indent, 0);
		return b.toString();
	}

	/**
	 * Convert Java {@link Collection} object to JSON string representing a
	 * list. Nested objects are indented.
	 * 
	 * @param collection
	 *            {@link Collection} of objects
	 * @param indent
	 *            number of spaces to indent nested values; if this is less than
	 *            0, there will be no indentation
	 * @return JSON string
	 * @throws JSONSimpleException
	 */
	public static <K extends Collection<?>> String convert(K collection,
			int indent) throws JSONSimpleException {
		StringBuilder b = new StringBuilder();
		convert(new ArrayList<Object>(collection), b, indent, 0);
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

		n = n.first(normTest);
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

	/**
	 * Normalizes whitespace in JSON string, indenting the contents of lists and
	 * maps appropriately. The indentation size is given by
	 * {@link #DEFAULT_INDENTATION}.
	 * 
	 * @param json
	 * @return json with unnecessary whitespace removed
	 * @throws JSONSimpleException
	 */
	public static String pretty(String json) throws JSONSimpleException {
		return pretty(json, DEFAULT_INDENTATION);
	}

	/**
	 * Normalizes whitespace in JSON string, indenting the contents of lists and
	 * maps appropriately.
	 * 
	 * @param json
	 * @param indent
	 *            indentation size in spaces; if this is less than 0, there will
	 *            be no indentation
	 * @return json with unnecessary whitespace removed
	 * @throws JSONSimpleException
	 */
	@SuppressWarnings("unchecked")
	public static String pretty(String json, int indent)
			throws JSONSimpleException {
		Object obj = convert(json);
		if (obj instanceof Map<?, ?>)
			return convert((Map<String, Object>) obj, indent);
		return convert((List<Object>) obj, indent);
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
		for (Match child : m.closest(stringOrValue)) {
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
		for (Match child : m.closest(valueTest)) {
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

	private static void convert(Map<String, Object> map, StringBuilder b,
			int indent, int margin) throws JSONSimpleException {
		if (margin > 0)
			newline(b, indent, margin);
		b.append('{');
		boolean nonInitial = false;
		for (Entry<String, Object> e : map.entrySet()) {
			newline(b, indent, margin + 1);
			if (nonInitial)
				b.append(',');
			else
				nonInitial = true;
			convert(e.getKey(), b);
			if (indent > -1)
				b.append(' ');
			b.append(':');
			if (indent > -1)
				b.append(' ');
			convert(e.getValue(), b, indent, margin + 2);
		}
		newline(b, indent, margin);
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
	private static void convert(Object value, StringBuilder b, int indent,
			int margin) throws JSONSimpleException {
		if (value == null)
			b.append("null");
		else if (value instanceof String)
			convert((String) value, b);
		else if (value instanceof Number)
			convert((Number) value, b);
		else if (value instanceof Object[])
			convert((Object[]) value, b, indent, margin);
		else if (value instanceof List<?>)
			convert((List<Object>) value, b, indent, margin);
		else if (value instanceof Map<?, ?>)
			convert((Map<String, Object>) value, b, indent, margin);
		else if (value instanceof Boolean)
			b.append(value.toString());
		else
			throw new JSONSimpleException(
					"values must be strings, arrays, lists, maps, null, booleans, or numbers");
	}

	private static void convert(Number n, StringBuilder b) {
		b.append(n.toString());
	}

	private static void convert(Object[] array, StringBuilder b, int indent,
			int margin) throws JSONSimpleException {
		if (margin > 0)
			newline(b, indent, margin);
		b.append('[');
		boolean nonInitial = false;
		for (Object o : array) {
			newline(b, indent, margin + 1);
			if (nonInitial)
				b.append(',');
			else
				nonInitial = true;
			convert(o, b, indent, margin + 1);
		}
		newline(b, indent, margin);
		b.append(']');
	}

	private static void convert(List<Object> list, StringBuilder b, int indent,
			int margin) throws JSONSimpleException {
		if (margin > 0)
			newline(b, indent, margin);
		b.append('[');
		boolean nonInitial = false;
		for (Object o : list) {
			newline(b, indent, margin + 1);
			if (nonInitial)
				b.append(',');
			else
				nonInitial = true;
			convert(o, b, indent, margin + 1);
		}
		newline(b, indent, margin);
		b.append(']');
	}

	/**
	 * Adds an indent of the appropriate size for pretty-printing.
	 * 
	 * @param b
	 * @param indent
	 * @param margin
	 */
	private static void newline(StringBuilder b, int indent, int margin) {
		if (indent < 0)
			return;
		b.append('\n');
		for (int i = 0; i < margin; i++) {
			for (int j = 0; j < indent; j++)
				b.append(' ');
		}
	}
}
