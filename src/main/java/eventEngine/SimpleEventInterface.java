package eventEngine;

import java.util.ArrayList;

import logParser1.Line;

public interface SimpleEventInterface extends EventTypeInterface {

	public Event checkLine(Line myLine, ArrayList<Line> buffer);

}
