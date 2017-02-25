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

	ArrayList<Event> events;
	private final static Logger logger = Logger.getLogger(LogParser.class.getName());

	public static void main(String[] args) {

		logger.log(Level.WARNING, "Testing info level");

		// TODO Auto-generated method stub
		// Path
		// file=FileSystems.getDefault().getPath("C:\\Users\\rhaines\\Documents",
		// "build-log-zdis.txt");
		// Path file =
		// FileSystems.getDefault().getPath("C:\\Users\\rhaines\\Documents\\build-logs",
		// "build-log-gcc.txt");
		Path file = FileSystems.getDefault().getPath("C:\\Users\\rhaines\\Documents\\build-logs",
				"build-log-many-names-gcc.txt");
		// Path
		// file=FileSystems.getDefault().getPath("C:\\Users\\rhaines\\Documents",
		// "build-log.txt");
		// Path
		// file=FileSystems.getDefault().getPath("C:\\Users\\rhaines\\Documents",
		// "build-log-iar.txt");
		// Path
		// file=FileSystems.getDefault().getPath("C:\\Users\\rhaines\\Documents\\Engagements\\Imagination
		// Tech", "build-log.txt");
		Charset charset = Charset.forName("UTF-8");

		LineParser parser = new LineParser();

		ArrayList<SimpleEventInterface> events = new ArrayList<SimpleEventInterface>();
		ArrayList<CompoundEventInterface> checkers = new ArrayList<CompoundEventInterface>();

		SimpleEventType endOfFile = new SimpleEventType("End of file reached", "end_of_file");
		SimpleEventType startOfFile = new SimpleEventType("Start of Analysis", "start_of_file");

		events.add(new GapFinder());
		events.add(new OddCharacterFinder());
		events.add(endOfFile);
		events.add(startOfFile);
		EventTypeFactory factory = new EventTypeFactory(events, checkers);
		factory.generateCheckers(null);

		// events.addAll(factory.getEventList());

		// checkers.addAll(factory.getCheckerList());

		// compCheckers=addAll(factory.generateCompCheckers());

		ArrayList<Event> eventList = new ArrayList<Event>();
		ArrayList<Line> lineBuffer = new ArrayList<Line>();

		Event startOfFileEvent = new Event(new Line(), startOfFile, Event.Priority.INFO);
		for (CompoundEventInterface checker : checkers) {
			checker.processState(startOfFileEvent);
		}

		Integer lineNo = 1;
		try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
			String line = null;
			while ((line = reader.readLine()) != null) {

				Line myLine = parser.parseLine(lineNo, line);
				lineBuffer.add(myLine);
				if (lineBuffer.size() > 100)
					lineBuffer.remove(0);

				for (SimpleEventInterface eventType : events) {

					Event event = eventType.checkLine(myLine, lineBuffer);
					if (event != null) {

						eventList.add(event);
						for (CompoundEventInterface checker : checkers) {
							logger.log(Level.SEVERE,"Size of list:"+eventList.size());
							eventList.addAll(checker.processState(event));
							logger.log(Level.SEVERE,"Size of list2:"+eventList.size());
						}

					}

				}

				lineNo += 1;
				// System.out.println(line);
			}

			// Send end of File Event
			Event endOfFileEvent = new Event(new Line(), endOfFile, Event.Priority.INFO);
			eventList.add(endOfFileEvent);
			for (CompoundEventInterface checker : checkers) {
				logger.log(Level.SEVERE,"Size of list3:"+eventList.size());
				eventList.addAll(checker.processState(endOfFileEvent));
				logger.log(Level.SEVERE,"Size of list4:"+eventList.size());
			}

			HashMap<String, Event> map = new HashMap<String, Event>();
			for (Event e : eventList) {
	/*
				String eventDigest = e.getEventHash();

				if (eventDigest != null) {

					if (map.containsKey(eventDigest)) {
						continue;
					}
					map.put(eventDigest, e);

				}
*/
				System.out.println("Event at line:" + e.getPriority() + ":" + e.getLine().getLineNo() + " :"
						+ e.getDescription());
					
				
			
			}

		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}

	}

}
