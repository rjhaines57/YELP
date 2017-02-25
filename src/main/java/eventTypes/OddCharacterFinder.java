package eventTypes;

import java.util.ArrayList;
import java.util.HashMap;

import eventEngine.Event;
import eventEngine.SimpleEventInterface;
import eventEngine.Event.Priority;
import logParser1.Line;

public class OddCharacterFinder implements SimpleEventInterface {

	private final String summary;
	public OddCharacterFinder()
	{
		summary=new String("Non ASCII character found at position(s) <positions>");
	}
	
	@Override
	public Event checkLine(Line myLine, ArrayList<Line> buffer) {
		ArrayList<Integer[]> positions=new ArrayList<Integer[]>();
		
		for (int i = 0; i < myLine.getRawData().length(); i++)
		{
			char ch = myLine.getRawData().charAt(i);  
		
		    if ((ch < 32 && ch != 9) || ( ch > 127 && (ch !=0xFEFF)))
		    {
		    	positions.add(new Integer[] {i+1,(int)ch});
		    	
		    }
		    //Process char
		}
		if (!positions.isEmpty())
		{
			StringBuffer eventText=new StringBuffer();
			for (Integer[] index:positions)
			{
				eventText.append(" (char:"+index[0]+" offset:"+index[1]+")");
			}
				
			
			Event event=new Event(myLine,this,Event.Priority.HIGH);
	    	event.setLinePos(0);
	    	HashMap<String,String> map=new HashMap<String,String>();
	    	map.put("positions",eventText.toString());
	    	event.setEventMetaData(map);
			return event;
			
		}
		else
			return null;
		
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
	
}