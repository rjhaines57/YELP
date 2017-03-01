package eventEngine;

import java.util.ArrayList;

import logParser.Line;

public interface EventTypeInterface {
    
	public ArrayList<String> getDescription();
	public String getSummary();
	public String getEventName();
	
	public ArrayList<Event> checkLine(Line myLine, ArrayList<Line> buffer);
	public ArrayList<Event> processState(Event event);
	
}
