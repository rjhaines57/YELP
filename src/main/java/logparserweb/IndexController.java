package logparserweb;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import logParser.LogParser;

@Controller
public class IndexController {
	
	  @Autowired
	  private FileManagerService fileService;
	
	
	  @RequestMapping("/")
	    public String index(Model model) {
	
		  
		  
		  
		  /*
			Path logFile = FileSystems.getDefault().getPath("C:\\Users\\rhaines\\Documents\\build-logs",
					 "build-log-gcc.txt");

			
			LogParser instance = new LogParser();

			instance.setupEventTypes();

			instance.parseLogFile(logFile);

			//instance.displayEvents();
		*/	
		
		   
		   List<LogFile> list = fileService.getFileList();
			
		   model.addAttribute("files",list);
		   
		   //context.setVariable("events", eventMap.subMap(eventMap.lastKey()-500,eventMap.lastKey()));
		  //context.setVariable("line", lineBuffer.subList(lineBuffer.size() - 500, lineBuffer.size()));
		  
	        return "index";
	    }
	

}
