package logparserweb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;


@Service
public class FileManagerService {

	@Autowired
	private LogFileDao logFileDao;
	
	public Long createFile(String originalFileName, String description)
	{
		String logFileId="";
		// Check magic for a zip file?
	    //try {
	      LogFile logFile= new LogFile(originalFileName, description);
	      logFileDao.save(logFile);
	      logFileId = String.valueOf(logFile.getId());
	      return logFile.getId();
	   // }
		
	   /* catch (Exception ex) {
	    	System.out.print(ex.toString());
	        return (-1L);
	      }*/
	}


	public void setFileName(Long id, String fileName)
	{
		LogFile logfile=logFileDao.findById(id);
		logfile.setFileName(fileName);
		
	}	


	public List<LogFile> getFileList()
	{
		
		ArrayList<LogFile> list=Lists.newArrayList(logFileDao.findAll());
		
		return list; 
		
	}	

	
		
		
	
	
	
	
}
