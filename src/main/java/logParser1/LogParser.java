package logParser1;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import eventEngine.CompoundEventInterface;
import eventEngine.Event;
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
	ArrayList<SimpleEventInterface> simpleEventTypes;
	ArrayList<CompoundEventInterface> compoundEventTypes;
	
	public void setupEventTypes() {

		simpleEventTypes = new ArrayList<SimpleEventInterface>();
		compoundEventTypes = new ArrayList<CompoundEventInterface>();

		// Builtin types
		simpleEventTypes.add(new GapFinder());
		simpleEventTypes.add(new OddCharacterFinder());

		// Get other types from ConfigFile
		EventTypeFactory factory = new EventTypeFactory(simpleEventTypes, compoundEventTypes);

		String checkerFileName="src\\main\\resources\\checkerDefinition.json";

		
		factory.generateCheckers(checkerFileName);

	}

	public void parseLogFile(Path logFile) {

		eventList=new ArrayList<Event>();
		Charset charset = Charset.forName("UTF-8");

		LineParser parser = new LineParser();

		SimpleEventType endOfFile = new SimpleEventType("End of file reached", "end_of_file");
		SimpleEventType startOfFile = new SimpleEventType("Start of Analysis", "start_of_file");

		
		ArrayList<Line> lineBuffer = new ArrayList<Line>();
		
		
		Event startOfFileEvent = new Event(new Line(), startOfFile, Event.Priority.INFO);
		for (CompoundEventInterface checker : compoundEventTypes) {
			checker.processState(startOfFileEvent);
		}

		Integer lineNo = 1;
		try (BufferedReader reader = Files.newBufferedReader(logFile, charset)) {
			String line = null;
			while ((line = reader.readLine()) != null) {

				Line myLine = parser.parseLine(lineNo, line);
				lineBuffer.add(myLine);
				if (lineBuffer.size() > 100)
					lineBuffer.remove(0);

				for (SimpleEventInterface eventType : simpleEventTypes) {

					ArrayList<Event> newEvents = eventType.checkLine(myLine, lineBuffer);

					if (newEvents != null) {
						for (Event event : newEvents) {
							eventList.add(event);
							for (CompoundEventInterface checker : compoundEventTypes) {

								eventList.addAll(checker.processState(event));

							}

						}
					}
				}

				lineNo += 1;
				// System.out.println(line);
			}

			// Send end of File Event
			Event endOfFileEvent = new Event(new Line(), endOfFile, Event.Priority.INFO);
			eventList.add(endOfFileEvent);
			for (CompoundEventInterface checker : compoundEventTypes) {
				
				eventList.addAll(checker.processState(endOfFileEvent));
				
			}

		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}

	

	}

	public void displayEvents()
	{

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
	
	public static void main(String[] args) {

		logger.log(Level.WARNING, "Testing info level");

		Path logFile = FileSystems.getDefault().getPath("C:\\Users\\rhaines\\Documents\\build-logs",
				"build-log-many-names-gcc.txt");

		LogParser instance = new LogParser();
		
		instance.setupEventTypes();
		
		instance.parseLogFile(logFile);
		
		instance.displayEvents();
		// TODO Auto-generated method stub
		// Path
		// file=FileSystems.getDefault().getPath("C:\\Users\\rhaines\\Documents",
		// "build-log-zdis.txt");
		// Path file =
		// FileSystems.getDefault().getPath("C:\\Users\\rhaines\\Documents\\build-logs",
		// "build-log-gcc.txt");

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
}
