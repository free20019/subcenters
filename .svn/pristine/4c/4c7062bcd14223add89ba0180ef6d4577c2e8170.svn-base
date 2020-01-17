package mvc.controllers;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mvc.service.PowerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PowerAction {

	@Autowired
	private PowerService powerService;
	
	@RequestMapping("/getPower")
	@ResponseBody
	public String getPower(HttpServletRequest request){
		HttpSession session = request.getSession();
		String username = String.valueOf(session.getAttribute("name"));
		System.out.println("----------------------------"+username+"--------------------");
		return powerService.getPower(username);
	}
	
	@RequestMapping("/getSession")
	@ResponseBody
	public String getSession(HttpServletRequest request){
		HttpSession session = request.getSession();
		String username = String.valueOf(session.getAttribute("name"));
		return username;
	}
	
	
}
