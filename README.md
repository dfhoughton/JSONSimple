JSONSimple
==========

David Houghton
5 October 2011

This is a tiny Java library for converting between JSON and Java collections. It is dependent on [dfh.grammar][grammar].

What it can do
--------------

1. This library can convert a JSON object or array into a Java `Map<String, Object>` or `List<Object>`.

2. It can convert a Java `Map<String, Object>`, `List<Object>`, or `Object[]`, into a JSON string.

3. The Objects must be of types `Map<String, Object>`, `List<Object>`, `Object[]`, `Integer`, `Number`, `String`, `Boolean`, or `null`. These are the types that may go in and will come out. In the case of `Number`, the subtype returned will be `BigDecimal`.

4. If you have a JSON string stripped of unnecessary whitespace and you want to make it human-readable, you can use the `String Converter.pretty(String)` method to add in missing whitespace and indentation. For example, it will convert the following rough JSON

    {"b"  : true, "a":1,  "grue":{"baz":1}, "c":null
    ,"d"  :	[],"eleemosynary" :1.5, 	"flynn": {"foo":"bar","quux"
    :
    [1,2,[3]]}}

into

    {"b":true,"a":1,"grue":{"baz":1},"c":null,"d":[],"eleemosynary":1.5,"flynn":{"foo":"bar","quux":[1,2,[3]]}}

5. If you have a puffy JSON string that you want to compress, removing all unnecessary whitespace, you can pass it through `String Converter.compress(String)`. For example, it will convert the rough JSON above into

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

License
-------

This software is distributed under the terms of the FSF Lesser Gnu Public License (see lgpl.txt).

[grammar]: http://dfhoughton.org/grammar/
