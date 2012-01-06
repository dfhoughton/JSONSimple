package dfh.json.simple.test;

import static org.junit.Assert.*;

import org.junit.Test;

import dfh.json.simple.Converter;
import dfh.json.simple.JSONSimpleException;

public class PrettyTest {

	@Test
	public void emptyList() throws JSONSimpleException {
		String json = "[]";
		String pretty = Converter.pretty(json);
		assertTrue("made \"pretty\" empty array", pretty.equals("[ ]"));
	}

	@Test
	public void list() throws JSONSimpleException {
		String json = "[true]";
		String pretty = Converter.pretty(json);
		assertTrue("single item list", pretty.equals("[ true ]"));
	}

	@Test
	public void emptyMap() throws JSONSimpleException {
		String json = "{}";
		String pretty = Converter.pretty(json);
		assertTrue("made \"pretty\" empty map", pretty.equals("{ }"));
	}

	@Test
	public void map() throws JSONSimpleException {
		String json = "{\"a\":0}";
		String pretty = Converter.pretty(json);
		assertTrue("single item list", pretty.equals("{ \"a\" : 0 }"));
	}

	@Test
	public void nested() throws JSONSimpleException {
		String json = "{\"a\": [true]}";
		String pretty = Converter.pretty(json);
		String comparison = "{ \"a\" : [ true ] }";
		assertTrue("single item list", pretty.equals(comparison));
	}

	@Test
	public void nested2() throws JSONSimpleException {
		String json = "{\"a\": [true,false], \"c\":1}";
		String pretty = Converter.pretty(json);
		StringBuilder b = new StringBuilder();
		while (b.length() < Converter.DEFAULT_INDENTATION)
			b.append(' ');
		String comparison = "{\n" + b + "\"a\" : \n" + b + b + "[\n" + b + b
				+ b + "true,\n" + b + b + b + "false\n" + b + b + "],\n" + b
				+ "\"c\" : 1\n}";
		assertTrue("multi-item indented", pretty.equals(comparison));
	}

	@Test
	public void nested3() throws JSONSimpleException {
		String json = "{\"a\": [true,false], \"c\":1}";
		String pretty = Converter.pretty(json, 4);
		StringBuilder b = new StringBuilder();
		while (b.length() < 4)
			b.append(' ');
		String comparison = "{\n" + b + "\"a\" : \n" + b + b + "[\n" + b + b
				+ b + "true,\n" + b + b + b + "false\n" + b + b + "],\n" + b
				+ "\"c\" : 1\n}";
		assertTrue("multi-item indented", pretty.equals(comparison));
	}

}
