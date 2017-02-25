package logParser1;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LineParser {

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
		//System.out.println("Failed to parse DateTime at line "+this.currentLine+" with content:"+dateString);	
		
		}
		
		return myTime;
	}
	
	
	Line parseLine(Integer lineNo, String line)
	{
		this.currentLine=lineNo;
		Line myLine = new Line();
		myLine.setRawData(line);
		myLine.setLineNo(currentLine);
		
		// Split string into list
		String[] elements=line.split("\\|");
		if (elements==null)
			return myLine;
		
		if (elements.length > 2)
		{
			// Likely to be a "normal" log line, try to fill in the usual stuff
			// like this: 2016-12-12T11:31:12.121713Z|cov-build|4685|info|> some stuff here
			if (elements[0]!=null)
					myLine.setDate(parseDate(elements[0]));
			if (elements[1]!=null)
					myLine.setProcessName(elements[1]);
			
			if (elements[2]!=null)
			{
			try {
			myLine.setPID(Integer.parseInt(elements[2]));
			} catch (NumberFormatException e)
			{
			//	System.out.println("Failed to parse Integer at line "+this.currentLine+" with content:"+elements[2]);
			}
			}
			
			if (elements.length>3 && elements[3]!=null)
				myLine.setLevel(elements[3]);
			return myLine;
		}
		
		String[] elements2=line.split("\\[|\\]");
		if (elements2.length>2)
		{
		try {
			myLine.setPID(Integer.parseInt(elements2[1]));
			} catch (NumberFormatException e)
			{
				//System.out.println("Failed to parse Integer at line "+this.currentLine+" with content:"+elements2[1]);
			}
		}
			
				
	return myLine;	
	}
	
	
}
