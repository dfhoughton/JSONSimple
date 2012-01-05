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
		assertTrue("made \"pretty\" empty array", pretty.equals("[\n]"));
	}

	@Test
	public void list() throws JSONSimpleException {
		String json = "[true]";
		String pretty = Converter.pretty(json);
		StringBuilder b = new StringBuilder();
		while (b.length() < Converter.DEFAULT_INDENTATION)
			b.append(' ');
		assertTrue("single item list", pretty.equals("[\n" + b + "true\n]"));
	}

	@Test
	public void map() throws JSONSimpleException {
		String json = "{\"a\":0}";
		String pretty = Converter.pretty(json);
		StringBuilder b = new StringBuilder();
		while (b.length() < Converter.DEFAULT_INDENTATION)
			b.append(' ');
		assertTrue("single item list",
				pretty.equals("{\n" + b + "\"a\" : 0\n}"));
	}

	@Test
	public void nested() throws JSONSimpleException {
		String json = "{\"a\": [true]}";
		String pretty = Converter.pretty(json);
		StringBuilder b = new StringBuilder();
		while (b.length() < Converter.DEFAULT_INDENTATION)
			b.append(' ');
		String comparison = "{\n" + b + "\"a\" : \n" + b + b + "[\n" + b + b
				+ b + "true\n" + b + b + "]\n}";
		assertTrue("single item list", pretty.equals(comparison));
	}

	@Test
	public void nested2() throws JSONSimpleException {
		String json = "{\"a\": [true]}";
		String pretty = Converter.pretty(json, 4);
		StringBuilder b = new StringBuilder();
		while (b.length() < 4)
			b.append(' ');
		String comparison = "{\n" + b + "\"a\" : \n" + b + b + "[\n" + b + b
				+ b + "true\n" + b + b + "]\n}";
		assertTrue("single item list", pretty.equals(comparison));
	}

}
