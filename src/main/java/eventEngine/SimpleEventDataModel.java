package eventEngine;

import java.util.ArrayList;
import java.util.logging.Logger;

import eventEngine.Event.Priority;
import logParser.RegexHelper;

public class SimpleEventDataModel {

	private static final Logger logger = Logger.getLogger(SimpleEventDataModel.class.getName());
	
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