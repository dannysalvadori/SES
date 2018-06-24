package com.fdmgroup.ses.controllerTest;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

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

import com.fdmgroup.ses.controller.AdminCRUDUserController;
import com.fdmgroup.ses.model.Role;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.RoleRepository;
import com.fdmgroup.ses.repository.UserRepository;
import com.fdmgroup.ses.service.UserService;
import com.fdmgroup.ses.validation.SesValidationException;
import com.fdmgroup.ses.validation.ValidationUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // Allows detached entities (caused by multiple entityManager instances) to be persisted
public class AdminCRUDUserControllerTest {
	
	private static final int STATUS_ACTIVE = 1;
	private static final int STATUS_INACTIVE = 0;

	@Autowired
	private AdminCRUDUserController ctrl;

	@Autowired
	RoleRepository roleRepo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	UserService userService;

	@Mock
	WebRequest webRequest;

	private ModelAndView mav = new ModelAndView();
	private final String VALID_TEST_EMAIL = String.valueOf(Math.random()) + "@sesTest.com";
	private final String ALTERNATE_VALID_TEST_EMAIL = String.valueOf(Math.random()) + "@sesTest2.com";

	@Before
	public void setUp() throws Exception {
		User testUser = userRepo.findByEmail(VALID_TEST_EMAIL);
		assertEquals("A user already exists with test email 1. Consider re-running test", null, testUser);
		testUser = userRepo.findByEmail(ALTERNATE_VALID_TEST_EMAIL);
		assertEquals("A user already exists with test email 2. Consider re-running test", null, testUser);
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
		testUser = userRepo.findByEmail(ALTERNATE_VALID_TEST_EMAIL);
		if (testUser != null) {
			userService.deleteUser(testUser);
		}
	}

	/*******************************************
	 * Tests Start *
	 *******************************************/

	/**
	 * manageUsers() adds all users to model (as "users") and sets the view to
	 * admin/manageUsers.jsp
	 */
	@Test
	public void goToManageUsersTest() {
		mav = ctrl.goToManageUsers(mav);
		assertEquals("Wrong view name", "admin/manageUsers", mav.getViewName());
		Object usersModelObject = mav.getModel().get("users");
		assertNotEquals(null, usersModelObject);
		assertTrue("newUser is wrong type", usersModelObject instanceof Collection<?>);
		Collection<?> users = (Collection<?>) usersModelObject;
		assertEquals("Didn't find all users", users.size(), userRepo.findAll().size());
		for (Object obj : users) {
			assertTrue("'User' is wrong type", obj instanceof User);
		}
	}

	/**
	 * createUser() adds a new User instance to the model (as "newUser") and sets
	 * the view to admin/createUser.jsp
	 */
	@Test
	public void goToCreateUserTest() {
		mav = ctrl.goToCreateUser(mav);
		assertEquals("Wrong view name", "admin/createUser", mav.getViewName());
		assertNotEquals(null, mav.getModel().get("newUser"));
		assertTrue("newUser is wrong type", mav.getModel().get("newUser") instanceof User);
	}

	/**
	 * If validation succeeds, createUser() inserts the given User object into the
	 * DB
	 */
	@Test
	public void doCreateUserSuccessTest() {
		User testUser = createUser();
		mav = ctrl.doCreateUser(mav, testUser, webRequest);

		// Confirm validation passes
		Object failures = mav.getModel().get("failures");
		assertEquals("Validation failed", null, (String) failures);

		User resultUser = userRepo.findByEmail(VALID_TEST_EMAIL);
		assertTrue("Null user", resultUser != null);
		assertEquals("Wrong email", VALID_TEST_EMAIL, resultUser.getEmail());
		assertEquals("Wrong amount of credit", new BigDecimal(50000), resultUser.getCredit());
	}

	/**
	 * If the user's password doesn't match the confirmation password, createUser()
	 * must not persist the given User object.
	 */
	@Test
	public void doCreateUserPasswordMismatchTest() {
		User testUser = createUser();
		testUser.setConfirmationPassword("Something different");
		mav = ctrl.doCreateUser(mav, testUser, webRequest);

		// Confirm validation failure
		Object failures = mav.getModel().get("failures");
		assertTrue("Validation didn't fail", ((String) failures).equals("Passwords do not match."));

		User resultUser = userRepo.findByEmail(VALID_TEST_EMAIL);
		assertTrue("Null user", resultUser == null);
	}

	/**
	 * If the user's password is too short, createUser() must not persist the given
	 * User object.
	 */
	@Test
	public void doCreateUserPasswordTooShortTest() {
		User testUser = createUser();
		testUser.setPassword("A");
		testUser.setConfirmationPassword("A");
		mav = ctrl.doCreateUser(mav, testUser, webRequest);

		Object failures = mav.getModel().get("failures");
		assertTrue("Validation didn't fail", ((String) failures).equals("Password must be 6 to 50 characters long."));

		User resultUser = userRepo.findByEmail(VALID_TEST_EMAIL);
		assertTrue("Null user", resultUser == null);
	}

	/**
	 * If the user's password is too long, createUser() must not persist the given
	 * User object.
	 */
	@Test
	public void doCreateUserPasswordTooLongTest() {
		// Generate a password of 60 chars length
		String tenChars = "XXXXXXXXXX";
		String longPassword = "";
		for (int i = 0; i < 6; i++) {
			longPassword += tenChars;
		}

		User testUser = createUser();
		testUser.setPassword(longPassword);
		testUser.setConfirmationPassword(longPassword);
		mav = ctrl.doCreateUser(mav, testUser, webRequest);

		Object failures = mav.getModel().get("failures");
		assertTrue("Validation didn't fail", ((String) failures).equals("Password must be 6 to 50 characters long."));

		User resultUser = userRepo.findByEmail(VALID_TEST_EMAIL);
		assertTrue("Null user", resultUser == null);
	}

	/**
	 * If the user's email has already been taken, createUser() must not persist the
	 * given User object.
	 */
	@Test
	public void doCreateUserAlreadyExistsTest() {
		// Insert user and confirm persistence
		User testUser = insertTestUser();

		// Attempt to insert another user with the same email
		testUser = createUser();
		mav = ctrl.doCreateUser(mav, testUser, webRequest);

		// Confirm validation failure
		Object failures = mav.getModel().get("failures");
		assertTrue("Validation didn't fail",
				((String) failures).equals("A user is already registered with this address."));
	}

	/**
	 * toggleActive() sets a user to active or inactive (whichever it wasn't before
	 * the toggle)
	 */
	@Test
	public void toggleActiveTest() {
		// Insert user and confirm persistence
		User testUser = insertTestUser();

		// Attempt to toggle user inactive
		mav = ctrl.toggleActive(mav, testUser.getId());
		testUser = userRepo.findByEmail(VALID_TEST_EMAIL);
		assertEquals("Wrong active status", STATUS_INACTIVE, testUser.getActive());

		// Toggle user active again
		mav = ctrl.toggleActive(mav, testUser.getId());
		testUser = userRepo.findByEmail(VALID_TEST_EMAIL);
		assertEquals("Wrong active status", STATUS_ACTIVE, testUser.getActive());
	}

	/**
	 * editUser() adds the User to be modified to the model as "user", adds all roles as "roles", and sets the view to 
	 * admin/editUser.jsp
	 */
	@Test
	public void goToEditUserTest() {
		User testUser = insertTestUser();
		mav = ctrl.goToEditUser(mav, testUser.getId());
		assertEquals("Wrong view name", "admin/editUser", mav.getViewName());
		assertNotEquals(null, mav.getModel().get("user"));
		assertTrue("user is wrong type", mav.getModel().get("user") instanceof User);
		User modelUser = (User) mav.getModel().get("user");
		assertEquals("Model User had wrong ID", testUser.getId(), modelUser.getId());
		assertEquals("Model User had wrong name", testUser.getName(), modelUser.getName());
		assertEquals("Model User had wrong email", testUser.getEmail(), modelUser.getEmail());
	}

	/**
	 * doEditUser() updates the User in the DB, and sets the view back to admin/manageUsers.jsp
	 */
	@Test
	public void doEditUserSuccessTest() {
		User testUser = insertTestUser();
		
		// Edit test user's details
		testUser.setName("ALTERED_NAME");
		mav = ctrl.doEditUser(mav, webRequest, testUser);
		
		// Get user from DB
		testUser = userRepo.findByEmail(VALID_TEST_EMAIL);
		
		assertEquals("Wrong view name", "admin/manageUsers", mav.getViewName());
		assertEquals("Model User had wrong ID", "ALTERED_NAME", testUser.getName());
	}
	
	/**
	 * doEditUser() fails if the email is changed to one that is already registered with another user
	 */
	@Test
	@Transactional
	public void doEditUserEmailAlteredToOneThatIsInUseTest() {
		User testUser = insertTestUser();
		
		// Insert a second test user with different email
		User secondUser = createUser();
		secondUser.setEmail(ALTERNATE_VALID_TEST_EMAIL);
		secondUser = insertTestUser(secondUser);
		
		// Edit test user's details
		testUser.setEmail(ALTERNATE_VALID_TEST_EMAIL);
		mav = ctrl.doEditUser(mav, webRequest, testUser);
		
		// Confirm validation failure
		Object failures = mav.getModel().get("failures");
		assertTrue("Validation didn't fail", failures != null);
		assertTrue("Wrong failures",
				((String) failures).equals("A user is already registered with this address."));
	}
	
	/**
	 * doEditUser() fails if the new password is too short
	 */
	@Test
	public void doEditUserPasswordTooShortTest() {
		// TODO
	}
	
	/**
	 * doEditUser() fails if the new password is too long
	 */
	@Test
	public void doEditUserPasswordTooLongTest() {
		// TODO
	}
	
	/**
	 * doEditUser() fails if the confirmation password doesn't match the new password
	 */
	@Test
	public void doEditUserTheConfirmationPasswordDoesntMatchTest() {
		// TODO
	}

	/*******************************************
	 * Helper Methods *
	 *******************************************/

	private User createUser() {
		User u = new User();
		u.setActive(1);
		u.setEmail(VALID_TEST_EMAIL);
		u.setPassword("123456");
		u.setConfirmationPassword("123456");
		u.setName("Testo");
		u.setLastName("McTest");
		Role roleUser = roleRepo.findByRole("ROLE_USER");
		Set<Role> roles = new HashSet<>();
		roles.add(roleUser);
		u.setRoles(roles);
		return u;
	}

	private User insertTestUser() {
		return insertTestUser(null);
	}
	private User insertTestUser(User u) {
		User testUser = u == null ? createUser() : u;
		try {
			userService.saveUser(testUser, webRequest);
		} catch (SesValidationException e) {
			assertTrue("Test user validation failure: " + ValidationUtils.stringifyFailures(e.getFailures()), false);
		}
		// Confirm successful persist
		User resultUser = userRepo.findByEmail(testUser.getEmail());
		System.out.println("Ins. id: " + testUser.getId());
		System.out.println("Ins. email: " + testUser.getEmail());
		assertTrue("Null user", resultUser != null);
		assertEquals("Wrong email", testUser.getEmail(), resultUser.getEmail());
		assertEquals("Wrong active status", STATUS_ACTIVE, resultUser.getActive());
		return resultUser;
	}
	
}
