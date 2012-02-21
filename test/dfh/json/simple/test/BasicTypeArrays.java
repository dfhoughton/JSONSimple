package dfh.json.simple.test;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import dfh.json.simple.Converter;
import dfh.json.simple.JSONSimpleException;

/**
 * Tests whether basic numeric type arrays -- int[], double[], etc. -- can be
 * converted into JSON.
 * <p>
 * 
 * @author David F. Houghton - Feb 21, 2012
 * 
 */
public class BasicTypeArrays {

	@Test
	public void intAr() {
		try {
			Converter.convert(new int[] { 0, 1 });
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void longAr() {
		try {
			Converter.convert(new long[] { 0, 1 });
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void byteAr() {
		try {
			Converter.convert(new byte[] { 0, 1 });
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void doubleAr() {
		try {
			Converter.convert(new double[] { 0, 1 });
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void floatAr() {
		try {
			Converter.convert(new float[] { 0, 1 });
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void complex1() {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("a", new int[] { 0, 1 });
		map.put("b", new float[] { 0, 1 });
		map.put("c", null);
		try {
			Converter.convert(map);
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void complex2() {
		Object[] ar = { new int[] { 0, 1 }, new float[] { 0, 1 }, null, };
		try {
			Converter.convert(ar);
		} catch (JSONSimpleException e) {
			fail(e.getMessage());
		}
	}

}
