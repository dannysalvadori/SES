package com.fdmgroup.ses.validationTest;

import static org.mockito.Mockito.when;
import static com.fdmgroup.ses.testUtils.UserUtils.*;
import static com.fdmgroup.ses.validation.UserValidator.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.UserRepository;
import com.fdmgroup.ses.validation.UserValidator;

/**
 * Test for UserValidator:
 * # Email must not already be registered.
 * # Password must be between 6 and 50 characters (inclusive).
 * # Confirmation password must match password. 
 * This must be tested for both insert and update operations
 */
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserValidatorTest extends ValidationTest<UserValidator> {
	
	@Mock
	private UserRepository userRepo;
	
	public UserValidatorTest() {
		validator = new UserValidator();
	}
	
	/************************************************
	*                 Insert Tests                  *
	************************************************/
	
	/**
	 * If validation passes all criteria, no failures are thrown
	 */
	@Test
	public void insertSuccessTest() {
		User u = createUser();
		validator.setUser(u);
		// No expected failures
		runTest();
	}
	
	/**
	 * Validation fails if the user's email is already registered to an account
	 */
	@Test
	public void emailAlreadyRegisteredInsertTest() {
		User u = createUser();
		u.setEmail(VALID_EMAIL);
		validator.setUser(u);
		// Simulate email already having been registered by stubbing same user
		when(userRepo.findByEmail(VALID_EMAIL)).thenReturn(u);
		expectedFailures.add(FAIL_ALREADY_REGISTERED);
		runTest();
	}
	
	/**
	 * Validation fails if the password is too short
	 */
	@Test
	public void passwordTooShortInsertTest() {
		User u = createUser();
		u.setPassword(SHORT_PASSWORD);
		u.setConfirmationPassword(SHORT_PASSWORD);
		validator.setUser(u);
		expectedFailures.add(FAIL_PASSWORD_LENGTH);
		runTest();
	}
	
	/**
	 * Validation fails if the password is too long
	 */
	@Test
	public void passwordTooLongInsertTest() {
		User u = createUser();
		u.setPassword(LONG_PASSWORD);
		u.setConfirmationPassword(LONG_PASSWORD);
		validator.setUser(u);
		expectedFailures.add(FAIL_PASSWORD_LENGTH);
		runTest();
	}
	
	/**
	 * Validation fails if the password doesn't match the confirmation password
	 */
	@Test
	public void passwordMismatchInsertTest() {
		User u = createUser();
		u.setPassword(VALID_PASSWORD);
		u.setConfirmationPassword("Some mismatching password");
		validator.setUser(u);
		expectedFailures.add(FAIL_PASSWORD_MISMATCH);
		runTest();
	}
	
	/************************************************
	*                 Update Tests                  *
	************************************************/
	
	/**
	 * If validation passes all criteria, no failures are thrown
	 */
	@Test
	public void updateSuccessTest() {
		// Define original details
		User oldUser = createUser();
		oldUser.setPassword(VALID_PASSWORD);
		oldUser.setEmail(VALID_EMAIL);
		
		// Define new details
		User newUser = createUser();
		newUser.setId(1); // Existence of ID indicates an update
		newUser.setPassword(ALT_VALID_PASSWORD);
		newUser.setConfirmationPassword(ALT_VALID_PASSWORD);
		newUser.setEmail(ALT_VALID_EMAIL);
		
		// Stub connection between the new and old versions of the user
		when(userRepo.findById(newUser.getId())).thenReturn(oldUser);
		
		validator.setUser(newUser);
		// No failures
		runTest();
	}	

	/**
	 * Validation fails if the user's email is already registered to an account
	 */
	@Test
	public void emailAlreadyRegisteredUpdateTest() {
		User oldUser = createUser();
		oldUser.setEmail(VALID_EMAIL);
		User newUser = createUser();
		newUser.setId(1); // Existence of ID indicates an update
		newUser.setEmail(ALT_VALID_EMAIL);
		when(userRepo.findById(newUser.getId())).thenReturn(oldUser);
		
		// Simulate email already having been registered by stubbing same user
		when(userRepo.findByEmail(ALT_VALID_EMAIL)).thenReturn(newUser);
		
		validator.setUser(newUser);		
		expectedFailures.add(FAIL_ALREADY_REGISTERED);
		runTest();
	}
	
	/**
	 * Validation fails if the password is too short
	 */
	@Test
	public void passwordTooShortUpdateTest() {
		User oldUser = createUser();
		User newUser = createUser();
		newUser.setId(1); // Existence of ID indicates an update
		newUser.setPassword(SHORT_PASSWORD);
		newUser.setConfirmationPassword(SHORT_PASSWORD);
		when(userRepo.findById(newUser.getId())).thenReturn(oldUser);
		validator.setUser(newUser);
		expectedFailures.add(FAIL_PASSWORD_LENGTH);
		runTest();
	}
	
	/**
	 * Validation fails if the password is too long
	 */
	@Test
	public void passwordTooLongUpdateTest() {
		User oldUser = createUser();
		User newUser = createUser();
		newUser.setId(1); // Existence of ID indicates an update
		newUser.setPassword(LONG_PASSWORD);
		newUser.setConfirmationPassword(LONG_PASSWORD);
		when(userRepo.findById(newUser.getId())).thenReturn(oldUser);
		validator.setUser(newUser);
		expectedFailures.add(FAIL_PASSWORD_LENGTH);
		runTest();
	}
	
	/**
	 * Validation fails if the password doesn't match the confirmation password
	 */
	@Test
	public void passwordMismatchUpdateTest() {
		User oldUser = createUser();
		oldUser.setPassword(VALID_PASSWORD);
		User newUser = createUser();
		newUser.setId(1); // Existence of ID indicates an update
		newUser.setPassword(ALT_VALID_PASSWORD);
		newUser.setConfirmationPassword("Some mismatching password");
		when(userRepo.findById(newUser.getId())).thenReturn(oldUser);
		validator.setUser(newUser);
		expectedFailures.add(FAIL_PASSWORD_MISMATCH);
		runTest();
	}

}
