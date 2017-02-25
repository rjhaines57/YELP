package eventTypes;

import java.util.ArrayList;

import eventEngine.Event;
import eventEngine.SimpleEventInterface;
import logParser1.Line;

public class SimpleEventType implements SimpleEventInterface {

	private String summary;
	private String eventName;
	
	public SimpleEventType(String summary,String eventName)
	{
		this.summary=summary;
		this.eventName=eventName;
				
	}
	
	@Override
	public ArrayList<String> getDescription() {
		ArrayList<String> tmp=new ArrayList<String>();
		tmp.add(this.summary);
		return tmp;		// TODO Auto-generated method stub
	}

	@Override
	public String getSummary() {
		return summary;
	}

	@Override
	public Event checkLine(Line myLine, ArrayList<Line> buffer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEventName() {
		// TODO Auto-generated method stub
		return eventName;
	}

	public void setEventName(String eventName)
	{
		this.eventName=eventName;
		
	}
	
}
