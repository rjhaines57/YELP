package eventEngine;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eventTypes.CompoundChecker;
import logParser.Line;

public class Event {

	private static final Logger logger = Logger.getLogger(Event.class.getName());

	public enum Priority {
		HIGH, MEDIUM, LOW, INFO
	}

	protected Line line;
	protected List<String> description;
	protected String summary;
	private Priority priority;
	private Integer linePos;
	protected HashMap<String, String> eventMetaData;
	protected EventTypeInterface eventType;
	protected List<Line> capturedLines;

	public Event(Line line, EventTypeInterface eventType, Priority priority) {
		this.line = line;
		this.priority = priority;
		this.eventType = eventType;
		this.capturedLines=new ArrayList<Line>();
		this.eventMetaData=new HashMap<>();
		}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	protected List<String> fillOutTemplate(List<String> descriptionTemplate) {

		
		if (this.eventMetaData == null)
			return descriptionTemplate;
		
		logger.log(Level.INFO,"fillOutTemplate for:"+eventType.getEventName());
		
		boolean appendCapturedLines = false;
		ArrayList<String> newDescription = new ArrayList<String>();
		for (String descriptionLine : descriptionTemplate) {
			// Meta data exists, try to do some substitutions
			Integer regexStartIndex = 0;
			Integer lastStartIndex = 0;
			StringBuffer newDescriptionLine = new StringBuffer();
			Pattern match = Pattern.compile("<[\\w]+>");
			
			if (match==null)
			{
				logger.log(Level.SEVERE,"match is null WTF!:"+descriptionLine);
				continue;
			}
			
			
			Matcher regexMatcher = match.matcher(descriptionLine);

			if (regexMatcher==null)
			{
				logger.log(Level.WARNING,"regexMatch is null :"+descriptionLine);
				continue;
			}
			
			while (regexMatcher.find()) {
				newDescriptionLine.append(descriptionLine.substring(lastStartIndex, regexMatcher.start()));
				lastStartIndex = regexMatcher.end();
				String field = regexMatcher.group(0).substring(1, regexMatcher.group(0).length() - 1);

				if (this.eventMetaData.containsKey(field)) {
					// logger.log(Level.INFO,"DEBUG2:"+this.eventMetaData.get(field));
					newDescriptionLine.append(this.eventMetaData.get(field));
				} else if (field.contentEquals("capturedLines")) {
					// logger.log(Level.INFO,"DEBUG2:"+this.eventMetaData.get(field));
					if (this.capturedLines != null) {
						appendCapturedLines = true;
						continue;
					}

				}
			}
			// Copy end of string
			if (lastStartIndex < descriptionLine.length() - 1) {
				newDescriptionLine.append(descriptionLine.substring(lastStartIndex, descriptionLine.length()));
			}

			if (appendCapturedLines) {
				for (Line line : this.capturedLines) {
					newDescription.add(line.getRawData());
				}
				appendCapturedLines=false;
			}
			else
			{
			
			newDescription.add(newDescriptionLine.toString());
			}
		}

	
	

	return newDescription;

	}

	public String getSummary() {
		ArrayList<String> summaryTemplate = new ArrayList<String>();
		if (this.summary != null) {
			summaryTemplate.add(this.summary);
		} else {
			summaryTemplate.add(this.eventType.getSummary());
		}

		return fillOutTemplate(summaryTemplate).get(0);
	}

	public List<String> getDescription() {
		List<String> descriptionTemplate;
		if (this.description != null) {
			descriptionTemplate = this.description;
		} else if (this.eventType.getDescription() != null) {
			descriptionTemplate = this.eventType.getDescription();
		} else {
			ArrayList<String> tmp = new ArrayList<String>();
			tmp.add(getSummary());
			return tmp;

		}
		return fillOutTemplate(descriptionTemplate);
	}

	public void setDescription(ArrayList<String> description) {
		this.description = description;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Integer getLinePos() {
		return linePos;
	}

	public void setLinePos(Integer linePos) {
		this.linePos = linePos;
	}

	public HashMap<String, String> getEventMetaData() {
		return eventMetaData;
	}

	public void setEventMetaData(HashMap<String, String> eventMetaData) {
		this.eventMetaData = eventMetaData;
	}

	public String getEventHash() {

		StringBuffer tmp = new StringBuffer();

		if (this.eventMetaData == null)
			return null;

		for (HashMap.Entry<String, String> entry : this.eventMetaData.entrySet()) {
			if (entry.getKey().contentEquals("rawData"))
				continue;
			if (entry.getKey().contentEquals("triggerText"))
				continue;

			tmp.append(entry.getKey());
			tmp.append(entry.getValue());
		}

		return tmp.toString();

	}

	public EventTypeInterface getEventType() {
		return eventType;
	}

	public List<Line> getCapturedLines() {
		return capturedLines;
	}

	public void setCapturedLines(List<Line> capturedLines) {
		this.capturedLines = capturedLines;
	}

}
