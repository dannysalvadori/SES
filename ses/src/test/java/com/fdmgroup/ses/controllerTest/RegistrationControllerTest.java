package com.fdmgroup.ses.controllerTest;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.controller.RegistrationController;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.RoleRepository;
import com.fdmgroup.ses.repository.UserRepository;
import com.fdmgroup.ses.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegistrationControllerTest {
	
	@Autowired
	private RegistrationController ctrl;
	
	@Autowired
	RoleRepository roleRepo;

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	UserService userService;
	
	@Mock
    WebRequest webRequest;
	
	private ModelAndView mav = new ModelAndView();
	private final String VALID_TEST_EMAIL = "danny.salvadori@gmail.com";//"example@sesTest.com";
	
	@Before
	public void setUp() throws Exception {
		User testUser = userRepo.findByEmail(VALID_TEST_EMAIL);
		assertEquals("A user already exists with the test email", null, testUser);
	}
	
	/**
	 * Delete any DB data persisted during the test 
	 */
	@After
	public void revert() {
		User testUser = userRepo.findByEmail(VALID_TEST_EMAIL);
		if (testUser != null) {
			userService.deleteUser(testUser);
		}
	}

	/**
	 * registerUser() adds a new User instance to the model (as "newUser") and sets the view to register.jsp
	 */
	@Test
	public void registerTest() {
		mav = ctrl.register(mav);
		assertEquals("Wrong view name", "register", mav.getViewName());
		assertNotEquals(null, mav.getModel().get("newUser"));
		assertTrue("newUser is wrong type", mav.getModel().get("newUser") instanceof User);
	}
	
	/**
	 * If validation succeeds, createUser() inserts the given User object into the DB
	 */
	@Test
	public void createUserSuccessTest() {
		User testUser = createUser();
		mav = ctrl.createUser(mav, testUser, webRequest);
		
		// Confirm validation passes
		Object failures = mav.getModel().get("failures");
		assertEquals("Validation failed", null, (String)failures);
		
		User resultUser = userRepo.findByEmail(VALID_TEST_EMAIL);
		assertTrue("Null user", resultUser != null);
		assertEquals("Wrong email", VALID_TEST_EMAIL, resultUser.getEmail());
		assertEquals("Wrong amount of credit", new BigDecimal(50000), resultUser.getCredit());
	}
	
	/**
	 * If the user's password doesn't match the confirmation password, createUser() must not persist
	 * the given User object.
	 */
	@Test
	public void createUserPasswordMismatchTest() {
		User testUser = createUser();
		testUser.setConfirmationPassword("Something different");
		mav = ctrl.createUser(mav, testUser, webRequest);
		
		// Confirm validation failure
		Object failures = mav.getModel().get("failures");
		assertTrue("Validation didn't fail", ((String)failures).equals("Passwords do not match."));
		
		User resultUser = userRepo.findByEmail(VALID_TEST_EMAIL);
		assertTrue("Null user", resultUser == null);
	}
	
	/**
	 * If the user's password is too short, createUser() must not persist the given User object.
	 */
	@Test
	public void createUserPasswordTooShortTest() {
		User testUser = createUser();
		testUser.setPassword("A");
		testUser.setConfirmationPassword("A");
		mav = ctrl.createUser(mav, testUser, webRequest);
		
		Object failures = mav.getModel().get("failures");
		assertTrue("Validation didn't fail", ((String)failures).equals("Password must be 6 to 50 characters long."));
		
		User resultUser = userRepo.findByEmail(VALID_TEST_EMAIL);
		assertTrue("Null user", resultUser == null);
	}
	
	/**
	 * If the user's password is too long, createUser() must not persist the given User object.
	 */
	@Test
	public void createUserPasswordTooLongTest() {
		// Generate a password of 60 chars length
		String tenChars = "XXXXXXXXXX";
		String longPassword = "";
		for (int i = 0; i < 6; i++) {
			longPassword += tenChars;
		}
		
		User testUser = createUser();
		testUser.setPassword(longPassword);
		testUser.setConfirmationPassword(longPassword);
		mav = ctrl.createUser(mav, testUser, webRequest);
		
		Object failures = mav.getModel().get("failures");
		assertTrue("Validation didn't fail", ((String)failures).equals("Password must be 6 to 50 characters long."));
		
		User resultUser = userRepo.findByEmail(VALID_TEST_EMAIL);
		assertTrue("Null user", resultUser == null);
	}
	
	/**
	 * If the user's email has already been taken, createUser() must not persist the given User object.
	 */
	@Test
	public void createUserAlreadyExistsTest() {
		// Insert user and confirm persistence
		User testUser = createUser();
		mav = ctrl.createUser(mav, testUser, webRequest);
		User resultUser = userRepo.findByEmail(VALID_TEST_EMAIL);
		assertTrue("Null user", resultUser != null);
		assertEquals("Wrong email", VALID_TEST_EMAIL, resultUser.getEmail());
		
		// Attempt to insert user with same email
		testUser = createUser();
		mav = ctrl.createUser(mav, testUser, webRequest);
		
		// Confirm validation failure
		Object failures = mav.getModel().get("failures");
		assertTrue("Validation didn't fail", ((String)failures).equals("A user is already registered with this address."));
	}
	
	private User createUser() {
		User u = new User();
		u.setId(1000); // This will be overridden
		u.setActive(1);
		u.setEmail(VALID_TEST_EMAIL);
		u.setPassword("123456");
		u.setConfirmationPassword("123456");
		u.setName("Testo");
		u.setLastName("McTest");
		return u;
	}

}
