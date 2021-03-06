package eventTypes;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import eventEngine.CompoundEvent;
import eventEngine.Event;
import eventEngine.SimpleEventInterface;
import eventEngine.Event.Priority;
import eventEngine.EventTypeInterface;
import logParser.Line;

public class GapFinder implements EventTypeInterface {

	private static final Logger logger = Logger.getLogger(GapFinder.class.getName());

	
	private ArrayList<String> description;
	private String summary;
	public static final int GAP=300;
	
	public GapFinder()
	{
		previousTime=null;
		description=new ArrayList<String>();
		description.add("Found gap of greater than "+GAP+" seconds");
		summary=new String("Found gap of greater than "+GAP+" seconds");
	}
	
	private LocalDateTime previousTime;
	
	@Override
	public ArrayList<Event> checkLine(Line myLine, ArrayList<Line> buffer) {
	
		// No previous time and current line has a date
		if (previousTime==null && myLine.getDate()!=null)
		{
				previousTime=myLine.getDate();
				return null;
		}
	
		
		if (myLine.getDate()!=null)
		{
			Long seconds = ChronoUnit.SECONDS.between(previousTime, myLine.getDate());
			previousTime=myLine.getDate();
			if (seconds>GAP)
			{
				Event event=new Event(myLine,this,Event.Priority.HIGH);
				return new ArrayList<Event>(Arrays.asList(event));
			}
						
		}
		
		
		
		return null;
	}

	@Override
	public ArrayList<String> getDescription() {
		return description;
	}

	@Override
	public String getSummary() {
		return summary;
	}

	@Override
	public String getEventName() {
		// TODO Auto-generated method stub
		return "gap_finder";
	}

	@Override
	public ArrayList<Event> processState(Event event) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
