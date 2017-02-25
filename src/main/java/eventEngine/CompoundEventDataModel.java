package eventEngine;

import java.util.HashMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CompoundEventDataModel {

	private static final Logger logger = Logger.getLogger(CompoundEventDataModel.class.getName());
	/*
	{
		"checkerName": "Missing Compiler Config",
		"transitions": 
		[
			{
				"state": "start",
				"event": "native_compile_executed",
				"next_state": "running"
			},

			{
				"state": "running",
				"event": "cov_emit_executed",
				"next_state": "finished"
			},

			{
				"state": "running",
				"event": "end_of_file",
				"next_state": "display"
			}
		],

		"summary": "The compiler (cc or c++) was invoked but it doesn't look like there was a corresponding"
	}
*/
	public String disable;
	public String checkerName;
	public Transitions[] transitions;
	public String summary;
	public ArrayList<String> description;
	public Event.Priority priority;
	
	public String eventKey;
	public class Transitions {
		public String state;
		public String event;
		public String next_state;
		public Comparison condition;
		
		public class Comparison {
			public String key;
			public String operation;
			public String value;
				
		}
		
		
	}

	public boolean validateEvents(ArrayList<SimpleEventInterface> eventList)
	{
		boolean allOk=true;
		for (Transitions transition:transitions)
		{
			boolean found=false;
			for (EventTypeInterface event:eventList)
			{
				if (event.getEventName().contentEquals(transition.event))
				{
					found=true;
					break;
				}
			}
			
			if (found==false)
			{
				allOk=false;
				logger.log(Level.SEVERE,"Cannot find event ["+transition.event+"] in checker:"+checkerName);
			}
			
		}
	
	return allOk;
	}
}
