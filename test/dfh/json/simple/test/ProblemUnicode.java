package dfh.json.simple.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import org.junit.BeforeClass;
import org.junit.Test;

import dfh.grammar.Grammar;

public class ProblemUnicode {

	private static String s;

	@BeforeClass
	public static void init() throws IOException {
		InputStream is = ProblemUnicode.class.getClassLoader()
				.getResourceAsStream("problem_unicode.txt");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int i;
		while ((i = is.read(buf)) > -1)
			baos.write(buf, 0, i);
		s = new String(baos.toByteArray(), "UTF-8").trim();
	}

	@Test
	public void test() {
		Grammar g = new Grammar(
				"ROOT = /[^ \\\\ \\t \\r \\f \\n \\x08 \" ]++/x");
		assertNotNull(g.matches(s).match());
	}

}
