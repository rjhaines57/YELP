package eventTypes;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eventEngine.Event;
import eventEngine.SimpleEventDataModel;
import eventEngine.TopEventDataModel;
import logParser1.Line;
import logParser1.RegexHelper;

public class RegexCheckerTest {

	private String testString;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.testString+= "	{\r\n" + 
				"		\"eventType\": \"regex\",\r\n" + 
				"		\"eventName\": \"compilation_warning\",\r\n" + 
				"		\"triggerRegex\": \"(?<type>(warning)) #(?<errNo>[0-9]+):\",\r\n" + 
				"		\"priority\": \"MEDIUM\",\r\n" + 
				"		\"summary\": \"Emit compilation <type> type <errNo> \"\r\n" + 
				"	},";
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCheckLine_Simple() {
		
		
		SimpleEventDataModel model=new SimpleEventDataModel();
		model.eventType="regex";
		model.eventName="test_regex";
		model.triggerRegex=new RegexHelper("(?<type>(warning)).*?#(?<errNo>[0-9]+):");
		model.priority=Event.Priority.MEDIUM;
		model.summary="test checker";
		RegexChecker regexChecker=new RegexChecker(model);
		Line myLine=new Line();
		myLine.setRawData("This is a warning line with number #56:");
		
		ArrayList<Line> buffer=new ArrayList<Line>();
		
		ArrayList<Event> events=regexChecker.checkLine(myLine,buffer);
		
		Event e=events.get(0);
		assert(e!=null);
		assert(e.getEventMetaData()!=null);
		assert(e.getEventMetaData().containsKey("type"));
		assertEquals(e.getEventMetaData().get("type"),"warning");
		assertEquals(e.getEventMetaData().get("errNo"),"56");
	}

	@Test
	public void testCheckLine_WithMetaData() {
		SimpleEventDataModel model=new SimpleEventDataModel();
		model.eventType="regex";
		model.eventName="test_regex";
		model.triggerRegex=new RegexHelper("(?<type>(warning)).*?#(?<errNo>[0-9]+):");
		model.priority=Event.Priority.MEDIUM;
		model.summary="test checker";
		RegexChecker regexChecker=new RegexChecker(model);
		Line myLine=new Line();
		myLine.setRawData("This is a warning line with number #56:");
		
		ArrayList<Line> buffer=new ArrayList<Line>();
		
		
ArrayList<Event> events=regexChecker.checkLine(myLine,buffer);
		
		Event e=events.get(0);
		assert(e!=null);
		assert(e.getEventMetaData()!=null);
		assert(e.getEventMetaData().containsKey("type"));
		assertEquals(e.getEventMetaData().get("type"),"warning");
		assertEquals(e.getEventMetaData().get("errNo"),"56");
	
		
	}
}
