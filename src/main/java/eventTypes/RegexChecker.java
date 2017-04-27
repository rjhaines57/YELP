package eventTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import eventEngine.CompoundEvent;
import eventEngine.Event;
import eventEngine.SimpleEventDataModel;
import eventEngine.EventTypeFactory;
import eventEngine.EventTypeInterface;
import eventEngine.SimpleEventInterface;
import eventEngine.Event.Priority;
import logParser.Line;
import logParser.RegexHelper;

public class RegexChecker implements EventTypeInterface {

	private static final Logger logger = Logger.getLogger(EventTypeFactory.class.getName());

	private SimpleEventDataModel config;
	private boolean moreToCapture;
	String triggerText;
	Line triggerLine;
	Integer readAheadCount;
	StringBuffer rawData;

	public RegexChecker() {
		moreToCapture = false;

	}

	public RegexChecker(SimpleEventDataModel config) {
		this.moreToCapture = false;
		this.config = config;
		this.readAheadCount = 1;
		rawData = new StringBuffer();

		if (this.config.linesToCapture == null)
			this.config.linesToCapture = new Integer(0);

		if (this.config.readBehind == null)
			this.config.readBehind = new Integer(0);

		this.config.priority = Event.Priority.INFO;

	}

	private HashMap<String,String> captureMetadata(List<Line> buffer) {

		HashMap<String,String> map=new HashMap<String,String>();
		
		// logger.log(Level.INFO,"Found error:");
		this.rawData.setLength(0);

		
		
		for (Line line : buffer) {
			logger.log(Level.FINE, "looking at line:" + line.getLineNo() + ":"+line.getRawData());
			this.rawData.append(line.getRawData()+"\r\n");
			if (config.captureRegexes != null) {
				for (RegexHelper regex : config.captureRegexes) {
					map.putAll(regex.matchGroups(line.getRawData()));
				}
			}
			map.putAll(config.triggerRegex.matchGroups(line.getRawData()));

			if (config.endCaptureRegex != null)
				map.putAll(config.endCaptureRegex.matchGroups(line.getRawData()));
			// System.out.println(line.getLineNo()+":"+line.getRawData());
		}
		
				
		
		return map;
	}

	@Override
	public ArrayList<Event> checkLine(Line myLine, ArrayList<Line> buffer) {
		// logger.log(Level.INFO,"Found error"+myLine.getRawData());

		Matcher triggerMatcher = this.config.triggerRegex.getPattern().matcher(myLine.getRawData());

		
		if (!this.moreToCapture) {
			if (triggerMatcher.find()) {
				this.triggerLine = myLine;
				this.triggerText = triggerMatcher.group(0);
				if (this.config.excludeRegex != null) {

					Matcher excludeMatcher = this.config.excludeRegex.getPattern().matcher(triggerMatcher.group(0));
					if (excludeMatcher.find()) {
						logger.log(Level.FINEST, "Excluding trigger matched:[" + excludeMatcher.group(0) + "]");
						return null;
					}
				}

				logger.log(Level.FINE, "CheckerType: [" + this.getEventName() + "] fired:" + this.triggerText + " at line ["+myLine.getLineNo()+"]");
				if (config.linesToCapture > 1 || config.endCaptureRegex != null) {
					if (!this.moreToCapture) {
						this.moreToCapture = true;
						logger.log(Level.FINEST, "CheckerType: [" + this.getEventName() + "] setting more to capture true"); 
						return null;
					}
				}
			} else
			{
			//logger.log(Level.FINE, "CheckerType: [" + this.getEventName() + "] no match:");
				return null;
			}
		} else {
			logger.log(Level.FINEST, "CheckerType: [" + this.getEventName() + "] looking for more lines readAheadCount:"+this.readAheadCount);
			Matcher endTriggerMatcher = null;
			if (this.config.endCaptureRegex != null) {
				endTriggerMatcher = this.config.endCaptureRegex.getPattern().matcher(myLine.getRawData());
			}

			if (endTriggerMatcher != null && config.linesToCapture == 0)
				config.linesToCapture = buffer.size() - 1;

			// Read the rest of the data
			if (readAheadCount < (config.linesToCapture - config.readBehind)
					|| (endTriggerMatcher != null && !endTriggerMatcher.find())) {
				logger.log(Level.FINEST, "CheckerType: [" + this.getEventName() + "] At line ["+myLine.getLineNo()+"] Lines still needed:"+(config.linesToCapture - config.readBehind));
				readAheadCount += 1;
				return null;
			}
		}

		this.moreToCapture = false;
		
		logger.log(Level.FINEST, "CheckerType: [" + this.getEventName() + "] creating event");

		List<Line> savedBuffer=buffer.subList((buffer.size())-this.readAheadCount-this.config.readBehind,buffer.size());
		logger.log(Level.FINEST, "CheckerType: [" + this.getEventName() + "] buffsize["+buffer.size()+"] readAheadCount["+this.readAheadCount+"] readBehind["+this.config.readBehind+"]");
		
		HashMap<String,String> map=captureMetadata(savedBuffer);

		this.readAheadCount = 1;

		
		Event event = new Event(triggerLine, this, config.priority);
		event.setEventMetaData(map);
		map.putAll(config.triggerRegex.matchGroups(this.triggerLine.getRawData()));
		map.put("triggerText", this.triggerText);
		
		if (config.fixPaths != null && config.fixPaths.contentEquals("true")) {
			
			for (Map.Entry<String, String> entry : map.entrySet()) {
				    String currentValue = entry.getValue();

				    entry.setValue(currentValue.replace("\\", "/"));
				}
				
				
			}

		
		map.put("rawData", this.rawData.toString());
		event.getCapturedLines().addAll(savedBuffer);

		
		
		return new ArrayList<Event>(Arrays.asList(event));

		
	}

	@Override
	public ArrayList<String> getDescription() {
		return config.description;
	}

	@Override
	public String getSummary() {
		
		
		return config.summary;
	}

	@Override
	public String getEventName() {
		// TODO Auto-generated method stub
		return config.eventName;
	}

	@Override
	public ArrayList<Event> processState(Event event) {
		// TODO Auto-generated method stub
		return null;
	}

}
