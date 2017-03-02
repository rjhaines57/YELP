package logParser;

import java.time.LocalDateTime;
import java.util.logging.Logger;


public class Line {

	private static final Logger logger = Logger.getLogger(Line.class.getName());

	private String rawData;
	private Integer lineNo;
	private LocalDateTime date;
	private String processName;
	private Integer PID;
	private String level;
	private String data;

	public Line() {
		date = null;
		processName = null;
		PID = null;
		level = null;
		data = null;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public Integer getPID() {
		return PID;
	}

	public void setPID(Integer pID) {
		PID = pID;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Integer getLineNo() {
		return lineNo;
	}

	public void setLineNo(Integer lineNo) {
		this.lineNo = lineNo;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

}
