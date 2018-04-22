package com.fdmgroup.service;

import com.fdmgroup.ses.model.User;

public interface UserService {
	public User findUserByEmail(String email);
	public void saveUser(User user);
	public void createVerificationToken(User user, String token);
}