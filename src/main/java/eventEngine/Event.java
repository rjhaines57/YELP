package eventEngine;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logParser1.Line;



public class Event {

	public enum Priority {
		HIGH,
		MEDIUM,
		LOW,
		INFO
	}

	
	protected	Line line;
	protected ArrayList<String> description;
	protected String summary;
	private Priority priority;
	private Integer linePos; 
	protected HashMap<String,String> eventMetaData;
	protected EventTypeInterface eventType;

	public Event(Line line,EventTypeInterface eventType,Priority priority)
	{
		this.line=line;
		this.priority=priority;
		this.eventType=eventType;
	}

	public Line getLine() {
		return line;
	}
	public void setLine(Line line) {
		this.line = line;
	}


	protected ArrayList<String> fillOutTemplate(ArrayList<String> descriptionTemplate)
	{

		if (this.eventMetaData==null)
			return descriptionTemplate;

		ArrayList<String> newDescription=new ArrayList<String>();
		for (String descriptionLine:descriptionTemplate)
		{
			// Meta data exists, try to do some substitutions
			Integer regexStartIndex=0;
			Integer lastStartIndex=0;
			String newDescriptionLine=new String();
			Pattern match=Pattern.compile("<[\\w]+>");
			Matcher regexMatcher=match.matcher(descriptionLine);
			while (regexMatcher.find())
			{
				newDescriptionLine+=descriptionLine.substring(lastStartIndex,regexMatcher.start());
				lastStartIndex=regexMatcher.end();
				String field=regexMatcher.group(0).substring(1, regexMatcher.group(0).length()-1);

				if (this.eventMetaData.containsKey(field))
				{
					// System.out.println("DEBUG2:"+this.eventMetaData.get(field));
					newDescriptionLine+=this.eventMetaData.get(field);
				}


			}
			// Copy end of string
			if (lastStartIndex<descriptionLine.length()-1)
			{
				newDescriptionLine=descriptionLine.substring(lastStartIndex,descriptionLine.length());
			}

			newDescription.add(newDescriptionLine);
		}

		return newDescription;
	}

	public String getSummary() {
		ArrayList<String> summaryTemplate=new ArrayList<String>();
		if (this.summary!=null)
		{
			summaryTemplate.add(this.summary);
		}
		else
		{
			summaryTemplate.add(this.eventType.getSummary());
		}

		return fillOutTemplate(summaryTemplate).get(0);
	}

	public ArrayList<String> getDescription() {
		ArrayList<String> descriptionTemplate;
		if (this.description!=null)
		{
			descriptionTemplate=this.description;
		}
		else if (this.eventType.getDescription()!=null)
		{
			descriptionTemplate=this.eventType.getDescription();
		}
		else
		{
			ArrayList<String> tmp=new ArrayList<String>();
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

	public String getEventHash()
	{

		String tmp=new String();

		if (this.eventMetaData==null)
			return null;


		for (HashMap.Entry<String, String> entry : this.eventMetaData.entrySet())
		{
			if (entry.getKey().contentEquals("rawData"))
				continue;
			if (entry.getKey().contentEquals("triggerText"))
				continue;
			
			tmp+=entry.getKey();
			tmp+=entry.getValue();
		}

		return tmp;


	}

	public EventTypeInterface getEventType() {
		return eventType;
	}


}
