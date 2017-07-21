package logParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexHelper {

	private final static Logger logger = Logger.getLogger(LogParser.class.getName());
	
	private String regex;
	private ArrayList<String> groups;
	private Pattern pattern;
	private final static Pattern captureNameFinder = Pattern.compile("\\?<([a-zA-Z][a-zA-Z0-9]*)>");;

	public RegexHelper()
	{
		
	}
	
	public RegexHelper(String regex) {
		this.regex = regex;
		this.groups = getCaptureGroups(regex);
		this.pattern = Pattern.compile(regex);
	}

	private ArrayList<String> getCaptureGroups(String regex) {
		ArrayList<String> groups = new ArrayList<String>();
		Matcher capture = this.captureNameFinder.matcher(regex);
		while (capture.find()) {
			for (Integer i = 1; i <= capture.groupCount(); i += 1) {
				logger.log(Level.INFO,"Found group in regex [" + this.regex + "] :[" + capture.group(i) + "]");
				groups.add(capture.group(i));
			}
			// logger.log(Level.INFO,"Here 1:"+capture.groupCount());

		}
		return groups;
	}

	public HashMap<String, String> matchGroups(String string) {

		HashMap<String, String> map = new HashMap<String, String>();
		Matcher regexMatcher = pattern.matcher(string);
		if (regexMatcher.find()) {
			for (String group : groups) {
				map.put(group, regexMatcher.group(group));
			}
		}
		if (map.isEmpty()) { return null; }
		return map;
	}

	public String getRegex() {
		return regex;
	}

	public ArrayList<String> getGroups() {
		return groups;
	}

	public Pattern getPattern() {
		return pattern;
	}

}
