package logParser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

import eventTypes.CompoundChecker;

public class LineParser {

	private static final Logger logger = Logger.getLogger(CompoundChecker.class.getName());
	
	private
		Integer currentLine;
	
	
	LocalDateTime parseDate(String dateString)
	{
		LocalDateTime myTime=null;
		try {
		myTime = LocalDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME);
		}
		catch (DateTimeParseException e)
		{
		//logger.log(Level.INFO,"Failed to parse DateTime at line "+this.currentLine+" with content:"+dateString);	
		
		}
		
		return myTime;
	}
	
	
	Line parseLine(Integer lineNo, String line)
	{
		this.currentLine=lineNo;
		Line myLine = new Line();
		myLine.setRawData(line);
		myLine.setLineNo(currentLine);
		
		int dateIndex=0;
		int processIndex=1;
		int pidIndex=2;
		int levelIndex=3;
		int dataIndex=4;
		
		boolean test1=true;
		// Split string into list
		String[] elements=line.split("\\|");
		if (elements==null || test1==true)
			return myLine;
		
		if (elements.length > 2)
		{
			// Likely to be a "normal" log line, try to fill in the usual stuff
			// like this: 2016-12-12T11:31:12.121713Z|cov-build|4685|info|> some stuff here
			if (elements[dateIndex]!=null)
			{
					myLine.setDate(parseDate(elements[dateIndex]));
			}
			if (elements[processIndex]!=null)
			{
					myLine.setProcessName(elements[dateIndex]);
			}
			
			if (elements[pidIndex]!=null)
			{
			try {
			myLine.setPID(Integer.parseInt(elements[pidIndex]));
			} catch (NumberFormatException e)
			{
			//	logger.log(Level.INFO,"Failed to parse Integer at line "+this.currentLine+" with content:"+elements[2]);
			}
			}
			

			if (elements.length>3 && elements[levelIndex]!=null)
			{
				myLine.setLevel(elements[levelIndex]);
			}

			if (elements.length>4 && elements[dataIndex]!=null)
			{
				myLine.setData(elements[levelIndex]);
			}
				
			return myLine;
		}
		
		String[] elements2=line.split("\\[|\\]");
		if (elements2.length>2)
		{
		try {
			myLine.setPID(Integer.parseInt(elements2[1]));
			} catch (NumberFormatException e)
			{
				//logger.log(Level.INFO,"Failed to parse Integer at line "+this.currentLine+" with content:"+elements2[1]);
			}
		}
			
				
	return myLine;	
	}
	
	
}
