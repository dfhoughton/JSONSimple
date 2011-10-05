package dfh.json.simple;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Converts between JSON and collections.
 * <p>
 * <b>Creation date:</b> Oct 5, 2011
 * 
 * @author David Houghton
 * 
 */
public class Converter {
	private Converter() {
	}

	public static String convert(Map<String, Object> collection)
			throws JSONSimpleException {
		StringBuilder b = new StringBuilder();
		convert(collection, b);
		return b.toString();
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
