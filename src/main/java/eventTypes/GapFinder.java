package eventTypes;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import eventEngine.Event;
import eventEngine.SimpleEventInterface;
import eventEngine.Event.Priority;
import logParser1.Line;

public class GapFinder implements SimpleEventInterface {
	
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
	public Event checkLine(Line myLine, ArrayList<Line> buffer) {
	
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
				return event;
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
	
	

}
