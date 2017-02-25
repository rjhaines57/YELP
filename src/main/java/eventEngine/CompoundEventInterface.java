package eventEngine;

import java.util.ArrayList;

public interface CompoundEventInterface extends EventTypeInterface {
	public ArrayList<CompoundEvent> processState(Event event);
}
