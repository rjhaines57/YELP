package eventEngine;

import java.util.ArrayList;

public interface CompoundEventInterface extends EventTypeInterface {
	public ArrayList<Event> processState(Event event);
}
