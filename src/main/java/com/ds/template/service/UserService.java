package com.ds.template.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ds.json.JsonModel;
import com.ds.template.dao.UserDao;
import com.ds.template.pojo.User;
import com.ds.template.utils.MemStore;
import com.ds.utils.StringUtils;

@Service("userService")
public class UserService {
	
	@Autowired
	private UserDao userDao;
	public int getUserSize() {
		return userDao.getUserSize();
	}
	public boolean hasAccount(String account) {
		if(StringUtils.isEmpty(account)) return false;
		List<User> users = userDao.getUsersByAccount(account);
		return !StringUtils.isEmpty(users);
	}
	public boolean hasNickName(String nickname) {
		if(StringUtils.isEmpty(nickname)) return false;
		List<User> users = userDao.getUsersByNickName(nickname);
		return !StringUtils.isEmpty(users);
	}
	public int createUser(User user) {
		if(user ==null) return -1;
		user.setDate(MemStore.getNowDateTime());
		return userDao.createUser(user);
	}
	
	public List<JsonModel> getUsers(int start,int count) {
		List<User> users = userDao.getUsers(start, count);
		return changeUser(users);
	}
	
	public JsonModel login(String account,String password) {
		User user = userDao.login(account, password);
		if(user!=null) {
			return user.toModel();
		}
		return null;
	}
	
	public List<JsonModel> changeUser(List<User> users) {
		List<JsonModel> list = new ArrayList<>();
		if(!StringUtils.isEmpty(users)) {
			for(User user : users) {
				if(user!=null) list.add(user.toModel());
			}
		}
		return list;
	}
}
