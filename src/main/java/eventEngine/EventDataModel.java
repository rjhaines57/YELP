package eventEngine;

import java.util.ArrayList;
import java.util.logging.Logger;

import eventEngine.Event.Priority;
import logParser1.RegexHelper;

public class EventDataModel {

	private static final Logger logger = Logger.getLogger(EventDataModel.class.getName());
	
	public String disable;
	public String eventType;
	public String eventName;

	public RegexHelper triggerRegex;
	public RegexHelper excludeRegex;
	public Event.Priority priority;

	public String summary;
	public ArrayList<String> description;

	public ArrayList<RegexHelper> captureRegexes;
	public Integer linesToCapture;
	public Integer readBehind;
	public RegexHelper endCaptureRegex;

	

}