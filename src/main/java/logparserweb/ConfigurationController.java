package logparserweb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ConfigurationController {

	@Autowired
	private ConfigurationService configService;
	
	@RequestMapping("/config")
	public String configApp(Model model) {
		
		
		return "config";
		
	}
	
	@RequestMapping(value = "/getconfig", produces="application/json")
	public @ResponseBody String getConfig()
	{
		
		return configService.getConfiguration();
	}
	
	
	
}
