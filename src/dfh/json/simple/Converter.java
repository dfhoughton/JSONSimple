package dfh.json.simple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import dfh.grammar.Grammar;
import dfh.grammar.GrammarException;
import dfh.grammar.Match;
import dfh.grammar.MatchTest;

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
			"   ROOT = <obj>",//
			"    obj = <s> '{' <s> [ <string> <s> ':' <s> <value> [ <s>  ',' <s> <string> <s> ':' <s> <value> <s> ]*+ <s> ]?+ '}' <s>",//
			"  array = '[' <s> [ <value> [ <s> ',' <s> <value> ]*+ <s> ]?+ ']'",//
			"  value = <string> | <obj> | <boolean> | <null> | <array> | <number>",//
			" string = '\"' [ '\\\\' <sc> | <nsc> ]*+ '\"'",//
			"boolean = 'true' | 'false'",//
			"     sc = <ec> | <ue>",//
			"     ec = /[rtfbn\\\\\\/\"]/",//
			"    nsc = /[^\\\\\\p{Cc}\"]/",//
			"   null = 'null'",//
			" number = /-?+(?:0|[1-9]\\d*+)(\\.\\d++)?+([eE][+-]?+\\d++)?+/",//
			"      s = /\\s*+/",//
			"     ue = /u[0-9a-fA-F]{4}/",//
	};
	/**
	 * {@link Grammar} generated from {@link #JSON_RULES}
	 */
	public static Grammar g;
	static {
		try {
			g = new Grammar(JSON_RULES);
		} catch (GrammarException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private Converter() {
	}

	public static String convert(Map<String, Object> collection)
			throws JSONSimpleException {
		StringBuilder b = new StringBuilder();
		convert(collection, b);
		return b.toString();
	}

	public static Map<String, Object> convert(String json)
			throws JSONSimpleException {
		Match m = g.matches(json).match();
		if (m == null)
			throw new JSONSimpleException("cannot parse " + json); // TODO make
																	// error
																	// more
																	// explanatory
		return convertObject(m, json);
	}

	private static Map<String, Object> convertObject(final Match m, String json) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		String key = null;
		for (Match child : m.getClosestDescendants(new MatchTest() {
			@Override
			public boolean test(Match m) {
				return m.hasLabel("string") || m.hasLabel("value");
			}
		})) {
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
		// value = <string> | <obj> | <boolean> | <null> | <array> |
		// <number>",//
		Match child = m.children()[0];
		if (child.hasLabel("string")) {
			return convertString(child, json);
		} else if (child.hasLabel("number")) {
			return convertNumber(child, json);
		} else if (child.hasLabel("obj")) {
			return convertObject(child, json);
		} else if (child.hasLabel("boolean")) {
			return convertBoolean(child, json);
		} else if (child.hasLabel("array")) {
			return convertArray(child, json);
		} else
			return null;
	}

	private static Object convertArray(Match m, String json) {
		List<Object> list = new ArrayList<Object>();
		for (Match child : m.getClosestDescendants(new MatchTest() {
			@Override
			public boolean test(Match o) {
				return o.hasLabel("value");
			}
		})) {
			list.add(convertValue(child, json));
		}
		return list;
	}

	private static Object convertBoolean(Match child, String json) {
		return new Boolean(json.substring(child.start(), child.end()));
	}

	private static Object convertNumber(Match child, String json) {
		return new Double(json.substring(child.start(), child.end()));
	}

	private static String convertString(Match m, String json) {
		StringBuilder b = new StringBuilder();
		for (Match child : m.get(new MatchTest() {
			@Override
			public boolean test(Match o) {
				return o.hasLabel("sc") || o.hasLabel("nsc");
			}
		})) {
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
				throw new RuntimeException("grammar out of sync with conversion code; unexpected escaped character " + s);
			}
		}
	}

	private static void convert(Map<String, Object> collection, StringBuilder b)
			throws JSONSimpleException {
		b.append('{');
		boolean nonInitial = false;
		for (Entry<String, Object> e : collection.entrySet()) {
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
