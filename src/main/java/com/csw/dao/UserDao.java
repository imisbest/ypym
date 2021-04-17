package com.csw.dao;

import com.csw.entity.User;

public interface UserDao {
	public void addUser(User user);
	public User selectUserByUsername(String username);
	public void changeUserStatus(Integer userId);
}
