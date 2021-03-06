package eventTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;

import eventEngine.CompoundEvent;
import eventEngine.Event;
import eventEngine.EventTypeInterface;
import eventEngine.Event.Priority;
import logParser.Line;

public class OddCharacterFinder implements EventTypeInterface {

	private static final Logger logger = Logger.getLogger(OddCharacterFinder.class.getName());

	
	private final String summary;
	public OddCharacterFinder()
	{
		summary=new String("Non ASCII character [<char>] found at position(s) <position>");
	}
	
	@Override
	public ArrayList<Event> checkLine(Line myLine, ArrayList<Line> buffer) {
		ArrayList<Integer[]> positions=new ArrayList<Integer[]>();
		
		ArrayList<Event> eventList=new ArrayList<Event>();
		
		for (int i = 0; i < myLine.getRawData().length(); i++)
		{
			char ch = myLine.getRawData().charAt(i);  
		
		    if ((ch < 32 && ch != 9) || ( ch > 127 && (ch !=0xFEFF)))
		    {
		    	
		    	Event event=new Event(myLine,this,Event.Priority.HIGH);
		    	event.setLinePos(0);
		    	HashMap<String,String> map=new HashMap<String,String>();
		    	map.put("position",String.valueOf(i));
		    	map.put("char", String.valueOf((int)ch));
		    	event.setEventMetaData(map);
		    	eventList.add(event);
		    }
		    //Process char
		}

		
		return eventList;
		
	}

	@Override
	public String getSummary() {
		
		return summary;
	}	


	@Override
	public ArrayList<String> getDescription() {
		
		ArrayList<String> tmp=new ArrayList<String>();
		tmp.add(this.summary);
		return tmp;
	}

	@Override
	public String getEventName() {
		// TODO Auto-generated method stub
		return "odd_character_finder";
	}

	@Override
	public ArrayList<Event> processState(Event event) {
		// TODO Auto-generated method stub
		return null;
	}	
	
}