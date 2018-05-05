package com.fdmgroup.ses.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.service.UserService;

@Component
public class UserValidator extends ModelValidator {
	
	@Autowired
	private UserService userService;

	private User user;

	/**
	 * Validates new User properties:
	 * # Email must not already be registered.
	 * # Password must be between 6 and 50 characters (inclusive).
	 * # Confirmation password must match password. 
	 */
	@Override
	public void validate() throws SesValidationException {
		
		failures.clear();
		
		if (userService.findUserByEmail(user.getEmail()) != null) {
			failures.add("A user is already registered with this address.");
		}
		
		if (user.getPassword().length() < 6 || user.getPassword().length() > 50) {
			failures.add("Password must be 6 to 50 characters long.");
		}

		if (!user.getPassword().equals(user.getConfirmationPassword())) {
			failures.add("Passwords do not match.");
		}
		
		throwFailures();
	}

	public void setUser(User user) {
		this.user = user;
	}

}