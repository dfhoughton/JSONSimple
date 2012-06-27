package dfh.json.example;

import dfh.json.simple.Converter;
import dfh.json.simple.JSONSimpleException;

public class BeautyAndConcision {

	private static final String roughJSON = "{\"b\"  : true, \"a\":1,  \"grue\":{\"baz\":1}, \"c\":null\n"
			+ ",\"d\"  :\t[],\"eleemosynary\" :1.5, "
			+ "\t\"flynn\": {\"foo\":\"bar\",\"quux\"\n:\n[1,2,[3]]}}";

	/**
	 * @param args
	 * @throws JSONSimpleException
	 */
	public static void main(String[] args) throws JSONSimpleException {
		System.out.println("rough JSON:");
		System.out.println(roughJSON);
		System.out.println("compact JSON:");
		System.out.println(Converter.compress(roughJSON));
		System.out.println("pretty JSON:");
		System.out.println(Converter.pretty(roughJSON));
	}

}
