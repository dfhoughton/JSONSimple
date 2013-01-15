package dfh.json.example;

import static dfh.json.simple.Converter.convert;

import java.util.Map;

public class Example {

	public static void main(String[] args) {
		String json = "{\"foo\": 1 }";
		System.out.println("original:       " + json);

		// convert to Java
		Map<String, Object> map = (Map<String, Object>) convert(json);

		// modify
		map.put("bar", "a b c d".split(" "));

		// back to JSON
		json = convert(map);
		System.out.println("modified:       " + json);

		// and again, just to prove we can parse this too
		map = (Map<String, Object>) convert(json);
		map.put("quux", null);
		json = convert(map);
		System.out.println("modified again: " + json);
	}

}
