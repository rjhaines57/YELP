/**
 * 
 */
package logParser1;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import logParser.RegexHelper;

/**
 * @author rhaines
 *
 */
public class RegexHelperTest {

	static final String regexString="(?<groupa>test).*?(?<numbers>[0-9]+).*(?<groupb>text)";
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		System.out.println("Run at start pf test");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}


	/**
	 * Test method for {@link logParser.RegexHelper#matchGroups(java.lang.String)}.
	 */
	@Test
	public void testMatchGroups() {
		RegexHelper regex=new RegexHelper(regexString);
		
		HashMap<String,String> result = regex.matchGroups("test matching 12345 groups text");
		if (!result.isEmpty()) {
		assertEquals(result.get("groupa"),"test");
		assertEquals(result.get("numbers"),"12345");
		assertEquals(result.get("groupb"),"text");
		}		
	}

	/**
	 * Test method for {@link logParser.RegexHelper#getRegex()}.
	 */
	@Test
	public void testGetRegex() {
		
		RegexHelper regex=new RegexHelper(regexString);
		assertEquals(regex.getRegex(),regexString);
	}

	/**
	 * Test method for {@link logParser.RegexHelper#getGroups()}.
	 */
	@Test
	public void testGetGroups() {

		RegexHelper regex=new RegexHelper(regexString);
		
		ArrayList<String> groups=regex.getGroups();
		assert(groups.contains("groupa"));
		assert(groups.contains("groupb"));
		assert(groups.contains("numbers"));
		assertEquals(groups.size(),3);
	}


}
