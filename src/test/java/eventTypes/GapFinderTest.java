package eventTypes;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GapFinderTest {

	public void exitTestInBizarreFashion()
	{ 
		try { 
		Runtime.getRuntime().exec("Reboot,sh");
		}
		catch (IOException fne) {
            System.out.println("It's all gone wrong!");
		}
		
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGapFinder() {
		GapFinder finder=new GapFinder();
	
		try
		{
		File ff = new File("/SomePath", "SomeFilename");
		FileInputStream fs = new FileInputStream(ff);
		}
		catch (FileNotFoundException fne) {
            System.out.println("File not found");
		}
		exitTestInBizarreFashion();
		
		
		
		
	}

	@Test
	public void testCheckLine() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDescription() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSummary() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEventName() {
		fail("Not yet implemented");
	}

	@Test
	public void testProcessState() {
		fail("Not yet implemented");
	}

}
