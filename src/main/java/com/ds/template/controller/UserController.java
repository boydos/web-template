package com.ds.template.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ds.json.JsonModel;
import com.ds.template.pojo.User;
import com.ds.template.service.UserService;
import com.ds.template.utils.JsonHelper;
import com.ds.template.utils.MemStore;
import com.ds.utils.Log;
import com.ds.utils.StringUtils;
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;
	
	@RequestMapping(value="goHome")
	public String goHome() {
		Log.d("login forward get");
		return "home";
	}
	@RequestMapping(value="getUsers",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getUsers(String start,String count) {
		if(!StringUtils.isNumber(start)) start="0";
		if(!StringUtils.isNumber(count)) count ="10";
		List<JsonModel> data =userService.getUsers(Integer.parseInt(start), Integer.parseInt(count));
		JsonModel response = JsonHelper.getSuccessModel("用户数据获取成功");
		response.set("data", data);
		response.set("total", userService.getUserSize());
		return response.toJson();
	}
	@RequestMapping(value="login",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String login(String account,String password) {
		if(StringUtils.isEmpty(account)||StringUtils.isEmpty(password)) {
			return JsonHelper.getError("账号密码不能为空");
		}
		String md5pass =StringUtils.EncoderByMd5(password);
		Log.d(String.format("account=%s password=%s", account,md5pass));
		
		JsonModel user = userService.login(account, md5pass);
		if(user!=null){
			String token =MemStore.getUUId();
			String tempKey = user.getString("id")+"_"+account;
			String lastToken = String.valueOf(MemStore.get(tempKey));
			MemStore.deleteKey(lastToken);
			MemStore.put("currentUser", user);
			MemStore.put(tempKey, token);
			JsonModel response = JsonHelper.getSuccessModel("登陆成功");
			response.set("token", token);
			response.set("name", user.getString("name"));
			response.set("id", user.getString("id"));
			
			String result = response.toJson();
			MemStore.put(token, result);
			return result;
		} else {
			return JsonHelper.getError("账号密码错误");
		}
	}
	@RequestMapping(value="register",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String register(User user) {
		if(StringUtils.isEmpty(user.getAccount())) {
			return JsonHelper.getError("用户账号不能为空");
		}
		if(StringUtils.isEmpty(user.getPassword())) {
			return JsonHelper.getError("用户密码不能为空");
		}
		if(StringUtils.isEmpty(user.getNickname())) {
			return JsonHelper.getError("用户昵称不能为空");
		}
		if(user.getRoleId() == -1) {
			user.setRoleId(0);
		}
		user.setPassword(StringUtils.EncoderByMd5(user.getPassword())); //md5 
		int ret =userService.createUser(user);
		if(ret>0) {
			return JsonHelper.getSuccess("用户注册成功");
		}
		return JsonHelper.getError("用户注册失败");
	}
}
