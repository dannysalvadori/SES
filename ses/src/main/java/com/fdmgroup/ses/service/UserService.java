package com.fdmgroup.ses.service;

import org.springframework.web.context.request.WebRequest;

import com.fdmgroup.ses.Validation.SesValidationException;
import com.fdmgroup.ses.model.User;

public interface UserService {
	public User findUserByEmail(String email);
	public void saveUser(User user, WebRequest request) throws SesValidationException;
	public void createVerificationToken(User user, String token);
}