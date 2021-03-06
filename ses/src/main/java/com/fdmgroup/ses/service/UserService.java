package com.fdmgroup.ses.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import com.fdmgroup.ses.email.EmailSender;
import com.fdmgroup.ses.email.ErrorEmail;
import com.fdmgroup.ses.model.Role;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.model.VerificationToken;
import com.fdmgroup.ses.registration.OnRegistrationCompleteEvent;
import com.fdmgroup.ses.repository.RoleRepository;
import com.fdmgroup.ses.repository.UserRepository;
import com.fdmgroup.ses.repository.VerificationTokenRepository;
import com.fdmgroup.ses.stockExchange.SaleForm;
import com.fdmgroup.ses.stockExchange.PurchaseForm;
import com.fdmgroup.ses.validation.SesValidationException;
import com.fdmgroup.ses.validation.ValidatorFactory;

@Service("userService")
public class UserService {

	@Autowired
	private UserRepository userRepo;
	@Autowired
    private RoleRepository roleRepo;
	@Autowired
	private VerificationTokenRepository vtRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	@Autowired
	private ValidatorFactory validationFactory;
	@Autowired
	private EmailSender emailSender;
	
	public void saveUser(User user, WebRequest request) throws SesValidationException {
		
		// If no role assigned, assume this is a new registration
		// Set ROLE_USER and active=0 (inactive)
		if (user.getRoles() == null || user.getRoles().isEmpty()) {
			Set<Role> userRoles = new HashSet<Role>();
			userRoles.add(roleRepo.findByRole("ROLE_USER"));
			user.setRoles(userRoles);
			if (userRoles == null || userRoles.isEmpty()) {
				SesValidationException vEx = new SesValidationException();
				vEx.addFailure("ROLE_USER could not be found in the db.");
				emailSender.sendEmail(new ErrorEmail(vEx));
				System.out.println("ROLE_USER could not be found in the db.");
			}
			user.setActive(0);
		}
		
		// Perform validations
		validationFactory.getValidator(user).validate();
		
		// Hash pw, then save user
		hashPassword(user);
		userRepo.save(user);
		
		// Create and email verification token
		String appUrl = request.getContextPath();
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), appUrl));
	}
	
	public void saveUser(User user) throws SesValidationException {
		// Perform validations
		validationFactory.getValidator(user).validate();
		
		// Save user
		hashPassword(user);
		userRepo.save(user);
	}

	/**
	 * Creates a unique verification token for a given User 
	 */
	public void createVerificationToken(User user, String token) {
		VerificationToken newToken = new VerificationToken(user, token);
		vtRepo.save(newToken);
	}

	public void deleteUser(User user) {
		userRepo.delete(user);
	}

	public User findCurrentUser() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userRepo.findByEmail(userDetails.getUsername());
	}

	public void updateCredit(User user, PurchaseForm transactionForm) {
		user.setCredit(user.getCredit().subtract(transactionForm.getTransactionValue()));
		userRepo.save(user);
	}

	public void updateCredit(User user, SaleForm saleForm) {
		user.setCredit(user.getCredit().subtract(saleForm.getTransactionValue()));
		userRepo.save(user);
	}
	
	/**
	 * Hashes the user's password as long as it is not already hashed.
	 * Depends on the limitation of a password's unhashed length to less than 60 characters 
	 */
	private void hashPassword(User user) {
		// Since PW length is limited to 50, but encrypted length is 60, this checks if PW is already encryped
		if (user.getPassword().length() != 60) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
	}

	public void activateUser(String token) throws SesValidationException {
		VerificationToken vt = vtRepo.findByToken(token);
		if (vt == null) {
			SesValidationException validationException = new SesValidationException();
			validationException.getFailures().add("That verification code is expired or incorrect!");
			throw validationException;
		}
		User user = vt.getUser();
		user.setActive(1);
		saveUser(user);
	}

}