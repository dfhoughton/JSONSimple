JSONSimple
==========

David Houghton
5 October 2011

This is a tiny Java library for converting between JSON and Java collections. It is dependent on [dfh.grammar][grammar].

What it can do
--------------

1. This library can convert a JSON object or array into a Java `Map<String, Object>` or `List<Object>`.

2. It can convert a Java `Map<String, Object>`, `List<Object>`, `Object[]`, or arrays of any of the basic types into a JSON string.

3. The values in lists and maps must be of types
   * `Map<String, Object>`
   * `List<Object>`
   * `Object[]`
   * `int[]`
   * `long[]`
   * `short[]`
   * `byte[]`
   * `double[]`
   * `float[]`
   * `char[]`
   * `String`
   * `Character`
   * `Number`
   * `boolean[]`
   * `Boolean`
   * `null`

   Of course, in the first three cases the `Object` must be some one of the more specific objects on the list. A `byte[]` is treated as a list of numbers. A `char[]` is treated as a list of strings. So as to avoid precision issues, JSON numbers are converted to either `BigInteger` or `BigDecimal` as appropriate.

4. If you have a JSON string stripped of unnecessary whitespace and you want to make it human-readable, you can use the `String Converter.pretty(String)` method to add in missing whitespace and indentation. For example, it will convert the following rough JSON

   ```json
   {"b"  : true, "a":1,  "grue":{"baz":1}, "c":null
   ,"d"  :	[],"eleemosynary" :1.5, 	"flynn": {"foo":"bar","quux"
   :
   [1,2,[3]]}}
   ```
   into

   ```json
   {
      "a"     : 1,
      "b"     : true,
      "c"     : null,
      "d"     : [ ],
      "eleemosynary" : 1.5,
      "flynn" : 
         {
            "foo"  : "bar",
            "quux" : 
               [
                  1,
                  2,
                  [ 3 ]
               ]
         },
      "grue"  : { "baz" : 1 }
   }
   ```

   The inspiration for this formatting style is [perltidy][], which in my experience produces the best code facelift of any code beautifier. Unlike perltidy, the JSONSimple beautifier has very limited configuration options: you can change the size of an indent.

5. If you have a puffy JSON string that you want to compress, removing all unnecessary whitespace, you can pass it through `String Converter.compress(String)`. For example, it will convert the rough JSON above into

   ```json
   {"b":true,"a":1,"grue":{"baz":1},"c":null,"d":[],"eleemosynary":1.5,"flynn":{"foo":"bar","quux":[1,2,[3]]}}
   ```
6. You get the basic functionality, converting between Java collections and JSON objects, with a single import:

   ```java
   import static dfh.json.simple.Converter.convert;
   ```

Example
-------

```java
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
```

Result:

    original:       {"foo": 1 }
    modified:       {"foo":1,"bar":["a","b","c","d"]}
    modified again: {"foo":1,"bar":["a","b","c","d"],"quux":null}

License
-------

This software is distributed under the terms of the FSF Lesser Gnu Public License (see lgpl.txt).

[grammar]: http://dfhoughton.org/grammar/
[perltidy]: http://perltidy.sourceforge.net/
