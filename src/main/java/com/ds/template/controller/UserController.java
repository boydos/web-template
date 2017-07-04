package com.ds.template.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.ds.utils.Log;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@RequestMapping(value="login",method=RequestMethod.POST)
	public String login() {
		Log.d("login");
		return "home";
	}
}
