package eventEngine;

import java.util.ArrayList;
import java.util.HashMap;

import logParser1.Line;

public class CompoundEvent extends Event {

	ArrayList<Event> events;

	public CompoundEvent(Line line, EventTypeInterface eventType, Event.Priority priority, ArrayList<Event> events) {
		super(line, eventType, priority);	
		
	
		this.events = events;
	}

	public ArrayList<String> getDescription() {

		ArrayList<String> descriptionTemplate;
		if (this.description != null) {
			descriptionTemplate = this.description;
		} else if (this.eventType.getDescription() != null) {
			descriptionTemplate = this.eventType.getDescription();
		} else {
			ArrayList<String> tmp = new ArrayList<String>();
			tmp.add(getSummary());
			for (Event event:events)
			{
				tmp.add(event.getSummary());
			
			}
			return tmp;

		}
		ArrayList<String> returnStrings=new ArrayList<String>();	
		returnStrings.add("HELLO!! THIS IS A CompoundEvent");
		returnStrings.addAll(fillOutTemplate(descriptionTemplate));
		
		for (Event event:events)
		{
			returnStrings.addAll(event.getDescription());
		
		}
		return returnStrings;
	}

	public String getEventHash() {

		String tmp = new String();

		for (Event event : this.events) {

			if (event.eventMetaData != null) {
				for (HashMap.Entry<String, String> entry : event.eventMetaData.entrySet()) {
					if (entry.getKey().contentEquals("rawData"))
						continue;
					if (entry.getKey().contentEquals("triggerText"))
						continue;

					tmp += entry.getKey();
					tmp += entry.getValue();
				}
			}
		}

		return tmp;

	}

	public ArrayList<Event> getEvents() {
		return events;
	}

}
