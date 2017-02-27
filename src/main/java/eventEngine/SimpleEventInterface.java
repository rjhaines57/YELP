package eventEngine;

import java.util.ArrayList;

import logParser.Line;

public interface SimpleEventInterface extends EventTypeInterface {

	public ArrayList<Event> checkLine(Line myLine, ArrayList<Line> buffer);

}
