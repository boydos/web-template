package com.ds.template.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
	
	
	@RequestMapping(value="loginOut",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String loginOut(String token,HttpServletRequest request) {
		Log.d("token="+token);
		if(!StringUtils.isEmpty(token)) {
			Object result =MemStore.get(token);
			if(result!=null) {
				JsonModel user = new JsonModel(result.toString());
				MemStore.deleteKey(user.getString("id")+"_"+user.getString("account"));
			}
			MemStore.deleteKey(token);
		}
		return JsonHelper.getSuccess("成功退出");
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
	public String login(String account,String password,HttpServletRequest request) {
		if(StringUtils.isEmpty(account)||StringUtils.isEmpty(password)) {
			return JsonHelper.getError("账号密码不能为空");
		}
		String md5pass =StringUtils.EncoderByMd5(password);
		Log.d(String.format("account=%s password=%s", account,md5pass));
		
		JsonModel user = userService.login(account, md5pass);
		if(user!=null){
			HttpSession session = request.getSession();
			String token =MemStore.getUUId();
			String tempKey = user.getString("id")+"_"+account;
			String lastToken = String.valueOf(MemStore.get(tempKey));
			//session.removeAttribute(lastToken);
			MemStore.deleteKey(lastToken);
			JsonModel response = JsonHelper.getSuccessModel("登陆成功");
			response.set("now", System.currentTimeMillis());
			response.set("token", token);
			response.set("account", account);
			response.set("nickname", user.getString("nickname"));
			response.set("id", user.getString("id"));
			String result = response.toJson();
			MemStore.put(tempKey, token);
			MemStore.put(token, result);
			//session.setAttribute(tempKey, token);
			//session.setAttribute(token, result);
			//session.setAttribute(MemStore.CURRENT_USER, user);
			return result;
		} else {
			return JsonHelper.getError("账号密码错误");
		}
	}
	@RequestMapping(value="verifyToken",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String verifyToken(String token,HttpServletRequest request) {
		Log.d("verify token="+token);
		Object result =MemStore.get(token);
		if(result !=null) {
			JsonModel user = new JsonModel(result.toString());
			long time = user.getLong("now", 0);
			if(System.currentTimeMillis() - time <= MemStore.TOKEN_TIMEOUT) {
				return result.toString();
			} else {
				MemStore.deleteKey(user.getString("id")+"_"+user.getString("account"));
				MemStore.deleteKey(token);
			}
		}
		return JsonHelper.getError("登陆过期，请重新登陆");
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
	@RequestMapping(value="deleteUser",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String deleteUser(String id) {
		if(StringUtils.isEmpty(id)) {
			return JsonHelper.getError("用户ID不能为空");
		}
		int ret=userService.deleteUser(Long.parseLong(id));
		if(ret>0) {
			return JsonHelper.getSuccess("用户删除成功");
		}
		return JsonHelper.getError("用户删除失败");
	}
	
	//-------------User Role--------------------
	
}
