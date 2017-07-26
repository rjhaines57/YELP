package logParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import com.google.gson.Gson;

import eventEngine.CompoundEventInterface;
import eventEngine.Event;
import eventEngine.CompoundEvent;
import eventEngine.EventTypeFactory;
import eventEngine.EventTypeInterface;
import eventEngine.SimpleEventInterface;
import eventTypes.GapFinder;
import eventTypes.OddCharacterFinder;
import eventTypes.RegexChecker;
import eventTypes.SimpleEventType;

public class LogParser {

	private final static Logger logger = Logger.getLogger(LogParser.class.getName());

	ArrayList<Event> eventList;
	SortedMap<Integer, ArrayList<Event>> eventMap;
	HashMap<String, EventTypeInterface> eventTypeMap;
	ArrayList<Line> lineBuffer;

	public void setupEventTypes() {

		
		int temp=0;
		lineBuffer = new ArrayList<Line>();
		eventTypeMap = new HashMap<String, EventTypeInterface>();
		eventMap = new TreeMap<Integer, ArrayList<Event>>();
		eventList = new ArrayList<Event>();
		// Builtin types
		temp=6;
		
		GapFinder gapFinder = new GapFinder();
		OddCharacterFinder charFinder = new OddCharacterFinder();

		eventTypeMap.put(gapFinder.getEventName(), gapFinder);
		eventTypeMap.put(charFinder.getEventName(), charFinder);

		SimpleEventType endOfFile = new SimpleEventType("End of file reached", "end_of_file");
		SimpleEventType startOfFile = new SimpleEventType("Start of Analysis", "start_of_file");
		eventTypeMap.put("end_of_file", endOfFile);
		eventTypeMap.put("start_of_file", startOfFile);
		// Get other types from ConfigFile
		EventTypeFactory factory = new EventTypeFactory(eventTypeMap);

		String checkerFileName = "src\\main\\resources\\checkerDefinition.json";

		factory.generateCheckers(checkerFileName);

	}

	public void parseLogFile(Path logFile) {

		Charset charset = Charset.forName("UTF-8");

		LineParser parser = new LineParser();
		List<Event> otherEventList = new ArrayList<>();

		Line startLine = new Line();
		startLine.setLineNo(0);
		startLine.setRawData("Start of file");
		Event startOfFileEvent = new Event(startLine, eventTypeMap.get("start_of_file"), Event.Priority.INFO);
		eventList.add(startOfFileEvent);
		ArrayList<Event> tmp = new ArrayList<Event>();
		tmp.add(startOfFileEvent);
		eventMap.put(startLine.getLineNo(), tmp);
		lineBuffer.add(startLine);

		for (EventTypeInterface eventType : eventTypeMap.values()) {
			eventType.processState(startOfFileEvent);
		}
		Integer logCount=1;
		Integer lineNo = 1;
		try (BufferedReader reader = Files.newBufferedReader(logFile, charset)) {
			String line = null;
			while ((line = reader.readLine()) != null) {

				
			
				Line myLine = parser.parseLine(lineNo, line);
				lineBuffer.add(myLine);
				
				if (lineBuffer.size() > 100000)
				lineBuffer.remove(0);

				
				ArrayList<Event> newSimpleEvents = new ArrayList<>();
				for (EventTypeInterface eventType : eventTypeMap.values()) {
					List<Event> returnedSimpleEvents = eventType.checkLine(myLine, lineBuffer);
					if (returnedSimpleEvents != null)
						newSimpleEvents.addAll(returnedSimpleEvents);
				}

				if (!newSimpleEvents.isEmpty()) {

					eventList.addAll(newSimpleEvents);
					if (eventMap.containsKey(myLine.getLineNo())) {
						eventMap.get(myLine.getLineNo()).addAll(newSimpleEvents);
					} else {
						ArrayList<Event> tmp4 = new ArrayList<Event>();
						tmp4.addAll(newSimpleEvents);
						eventMap.put(myLine.getLineNo(), tmp4);
					}
				
					
					for (Event event : newSimpleEvents) {

						for (EventTypeInterface eventType : eventTypeMap.values()) {
							ArrayList<Event> returnedComplexEvents = eventType.processState(event);
						//	if (returnedComplexEvents != null) {
							logger.log(Level.INFO, "complexEvents: "+returnedComplexEvents.size());
								if (eventMap.containsKey(myLine.getLineNo())) {
									eventMap.get(myLine.getLineNo()).addAll(returnedComplexEvents);
								} 
								else {
									ArrayList<Event> tmp4 = new ArrayList<Event>();
									tmp4.addAll(returnedComplexEvents);
									eventMap.put(myLine.getLineNo(), tmp4);
								}
								eventList.addAll(returnedComplexEvents);
						//	}
						}
					} 
					
				} 

				
				logCount=logCount+1;
				if (logCount % 10000 == 0)
				{
					logger.log(Level.INFO, "processing line :"+lineNo+" "+lineBuffer.size());
					logCount=1;
				}
				
				lineNo += 1;
				// System.out.println(line);
			}

			// Send end of File Event
			Line endofFileLine = new Line();
			endofFileLine.setRawData("End of File");
			endofFileLine.setLineNo(lineNo);
			Event endOfFileEvent = new Event(endofFileLine, eventTypeMap.get("end_of_file"), Event.Priority.INFO);
			eventList.add(endOfFileEvent);
			ArrayList<Event> tmp2 = new ArrayList<Event>();
			tmp2.add(endOfFileEvent);
			eventMap.put(endofFileLine.getLineNo(), tmp2);
			lineBuffer.add(endofFileLine);
			for (EventTypeInterface eventType : eventTypeMap.values()) {
				List<Event> newEndEvents = eventType.processState(endOfFileEvent);
				if (newEndEvents != null) {
					if (eventMap.containsKey(endofFileLine.getLineNo())) {
						eventMap.get(endofFileLine.getLineNo()).addAll(newEndEvents);
					} else {
						ArrayList<Event> tmp4 = new ArrayList<Event>();
						tmp4.addAll(newEndEvents);
						eventMap.put(endofFileLine.getLineNo(), tmp4);
					}
					eventList.addAll(newEndEvents);
				}

			}

		} catch (

		IOException x) {
			System.err.format("IOException: %s%n", x);
		}

	}

	public void displayEvents() {

		HashMap<String, Event> map = new HashMap<String, Event>();
		for (Event e : eventList) {

			String eventDigest = e.getEventHash();

			if (eventDigest != null) {

				if (map.containsKey(eventDigest)) {
					// continue;
				}
				map.put(eventDigest, e);

			}

			System.out.println(
					"Event at line:" + e.getPriority() + ":" + e.getLine().getLineNo() + " :" + e.getDescription());

		}

	}

	private TemplateEngine templateEngine;

	private void initialiseTemplateEngine() {
		templateEngine = new TemplateEngine();
		FileTemplateResolver fileTemplateResolver = new FileTemplateResolver();
		fileTemplateResolver.setPrefix("src/main/resources/templates/");
		fileTemplateResolver.setSuffix(".html");
		fileTemplateResolver.setTemplateMode("HTML5");
		templateEngine.setTemplateResolver(fileTemplateResolver);

	}

	public void outputasHTML() {
		BufferedWriter writer = null;

		try {
			OutputStreamWriter char_output = new OutputStreamWriter(new FileOutputStream("index.html"),
					Charset.forName("UTF-8").newEncoder());

			// writer = new BufferedWriter(new
			// FileWriter("index.html",Charset.forName("UTF-8").newEncoder()));
			initialiseTemplateEngine();
			Context context = new Context();
			context.setVariable("events", eventMap.subMap(eventMap.lastKey()-500,eventMap.lastKey()));
			context.setVariable("line", lineBuffer.subList(lineBuffer.size() - 500, lineBuffer.size()));
			templateEngine.process("index", context, char_output);

		} catch (IOException e) {
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
			}
		}

	}

	/*
	 * HashMap<String, Event> map = new HashMap<String, Event>(); for (Event e :
	 * eventList) {
	 * 
	 * String eventDigest = e.getEventHash();
	 * 
	 * if (eventDigest != null) {
	 * 
	 * if (map.containsKey(eventDigest)) { // continue; } map.put(eventDigest,
	 * e);
	 * 
	 * }
	 * 
	 * System.out.println( "Event at line:" + e.getPriority() + ":" +
	 * e.getLine().getLineNo() + " :" + e.getDescription());
	 * 
	 * }
	 */

	public static void main(String[] args) {

		logger.log(Level.WARNING, "Testing info level");

		Path logFile = FileSystems.getDefault().getPath("C:\\Users\\rhaines\\Documents\\build-logs",
				"build-log.txt");

	//	Path logFile = FileSystems.getDefault().getPath("C:\\Users\\rhaines\\Documents\\build-logs",
	//			 "build-log_kollol.txt");

		
		LogParser instance = new LogParser();

		instance.setupEventTypes();

		instance.parseLogFile(logFile);

		instance.displayEvents();

		
		Gson gson=new Gson();
		String events= gson.toJson(instance.getEventList());
		System.out.println("JSON:" + events);

		
	//	instance.outputasHTML();
		// TODO Auto-generated method stub
		// Path
		// file=FileSystems.getDefault().getPath("C:\\Users\\rhaines\\Documents",
		// "build-log-zdis.txt");
		// Path file =
		
		// Path
		// file=FileSystems.getDefault().getPath("C:\\Users\\rhaines\\Documents",
		// "build-log.txt");
		// Path
		// file=FileSystems.getDefault().getPath("C:\\Users\\rhaines\\Documents",
		// "build-log-iar.txt");
		// Path
		// file=FileSystems.getDefault().getPath("C:\\Users\\rhaines\\Documents\\Engagements\\Imagination
		// Tech", "build-log.txt");
	}

	public ArrayList<Event> getEventList() {
		return eventList;
	}

	public SortedMap<Integer, ArrayList<Event>> getEventMap() {
		return eventMap;
	}
}
