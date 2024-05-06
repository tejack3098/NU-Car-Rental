package neu.csye6200.crs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller 
public class HomeController {
	
	
	@GetMapping("")
	public String viewHomepage()

	{
		
		return "index";	
		
	}
	
}
