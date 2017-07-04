package com.ds.template.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ds.json.JsonModel;
import com.ds.template.pojo.User;
import com.ds.template.utils.JsonHelper;
import com.ds.utils.Log;
import com.ds.utils.StringUtils;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@RequestMapping(value="goHome")
	public String goHome() {
		Log.d("login forward get");
		return "home";
	}
	@RequestMapping(value="getUsers",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getUsers(String start,String count) {
		if(StringUtils.isEmpty(start)) start="0";
		if(StringUtils.isEmpty(count)) count ="10";
		JsonModel response = JsonHelper.getSuccessModel("用户数据获取成功");
		response.set("data", null);
		response.set("total", 100);
		return response.toJson();
	}
	@RequestMapping(value="login",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String login(String account,String password) {
		
		return JsonHelper.getError("用户登录失败");
	}
	@RequestMapping(value="register",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String register(User user) {
		StringUtils.EncoderByMd5(user.getPassword());
		
		return JsonHelper.getError("用户注册失败");
	}
}
