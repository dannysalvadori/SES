package com.fdmgroup.ses.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.UserRepository;

@Component
public class UserValidator extends ModelValidator {
	
	public static final String FAIL_PASSWORD_LENGTH = "Password must be 6 to 50 characters long.";
	public static final String FAIL_PASSWORD_MISMATCH = "Passwords do not match.";
	public static final String FAIL_ALREADY_REGISTERED = "A user is already registered with this address.";
	
	@Autowired
	private UserRepository userRepo;

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
		
		Boolean isUpdate = user.getId() != null;
		
		// Update
		if (isUpdate) {

			User oldUser = userRepo.findById(user.getId());
			
			// Fail if email is changed to a taken address
			if (!oldUser.getEmail().equalsIgnoreCase(user.getEmail())
					&& userRepo.findByEmail(user.getEmail()) != null
			) {
				failures.add(FAIL_ALREADY_REGISTERED);
			}
			
			// Fail if password is changed but is too short, or if confirmation doesn't match
			if (!oldUser.getPassword().equals(user.getPassword())) {
				if (user.getPassword().length() < 6 || user.getPassword().length() > 50) {
					failures.add(FAIL_PASSWORD_LENGTH);
				}
				
				if (!user.getPassword().equals(user.getConfirmationPassword())) {
					failures.add(FAIL_PASSWORD_MISMATCH);
				}
			}
			
		// Insert
		} else {
			
			if (userRepo.findByEmail(user.getEmail()) != null) {
				failures.add(FAIL_ALREADY_REGISTERED);
			}
			
			if (user.getPassword().length() < 6 || user.getPassword().length() > 50) {
				failures.add(FAIL_PASSWORD_LENGTH);
			}
	
			if (!user.getPassword().equals(user.getConfirmationPassword())) {
				failures.add(FAIL_PASSWORD_MISMATCH);
			}
		
		}
		throwFailures();
	}

	public void setUser(User user) {
		this.user = user;
	}

}