package com.fdmgroup.ses.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import com.fdmgroup.ses.Validation.SesValidationException;
import com.fdmgroup.ses.Validation.ValidationFactory;
import com.fdmgroup.ses.model.Role;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.model.VerificationToken;
import com.fdmgroup.ses.registration.OnRegistrationCompleteEvent;
import com.fdmgroup.ses.repository.RoleRepository;
import com.fdmgroup.ses.repository.UserRepository;
import com.fdmgroup.ses.repository.VerificationTokenRepository;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;
	@Autowired
    private RoleRepository roleRepo;
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Autowired
	ApplicationEventPublisher eventPublisher;
	
	@Autowired
	ValidationFactory validationFactory;
	
	@Override
	public User findUserByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	@Override
	public void saveUser(User newUser, WebRequest request) throws SesValidationException {
		
		// Set ROLE_USER
		Set<Role> userRoles = new HashSet<Role>();
		userRoles.add(roleRepo.findByRole("ROLE_USER"));
		newUser.setRoles(userRoles);
		
		// Perform validations
		validationFactory.getValidator(newUser).validate();
		
		// Save user
		userRepo.save(newUser);
		
		// Create and email verification token
		String appUrl = request.getContextPath();
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(newUser, request.getLocale(), appUrl));
	}

	/**
	 * Creates a unique verification token for a given User 
	 */
	@Override
	public void createVerificationToken(User user, String token) {
		System.out.println("user: " + user);
		System.out.println("token: " + token);
		VerificationToken newToken = new VerificationToken(user, token);
		verificationTokenRepository.save(newToken);		
	}

}