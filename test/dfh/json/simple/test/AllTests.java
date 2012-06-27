package dfh.json.simple.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Holds all unit tests written for library.
 * <p>
 * <b>Creation date:</b> Oct 5, 2011
 * 
 * @author David Houghton
 * 
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
//
		BasicConversions.class,//
		StringToObject.class,//
		ParseErrors.class,//
		CompressTest.class,//
		PrettyTest.class,//
		ProblemUnicode.class,//
		BasicTypeArrays.class,//
		Regressions.class//
})
public class AllTests {
}
