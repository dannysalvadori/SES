package com.fdmgroup.ses.controllerTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.fdmgroup.ses.utils.UserUtils.*;
import static com.fdmgroup.ses.utils.RoleUtils.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.controller.AdminCRUDUserController;
import com.fdmgroup.ses.model.Role;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.RoleRepository;
import com.fdmgroup.ses.repository.UserRepository;
import com.fdmgroup.ses.service.UserService;
import com.fdmgroup.ses.validation.SesValidationException;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class AdminCRUDUserControllerTest {
	
	@InjectMocks
	private AdminCRUDUserController ctrl = new AdminCRUDUserController();
	private ModelAndView mav = new ModelAndView();

	@Mock
	private RoleRepository roleRepo;

	@Mock
	private UserRepository userRepo;

	@Mock
	private UserService userService;

	@Mock
	private WebRequest webRequest;
	
	@Mock
	WebDataBinder binder;
	
	private List<User> allUsers = new ArrayList<>();
	private List<Role> allRoles = new ArrayList<>();

	@Before
	public void setUp() throws Exception {
		setupAllUsers();
		setupAllRoles();
		when(userRepo.findAll()).thenReturn(allUsers);
		when(roleRepo.findAll()).thenReturn(allRoles);
	}
	
	private void setupAllUsers() {
		for (Integer i = 0; i < 3; i++) {
			User u = createUser();
			u.setEmail(u.getEmail() + i);
			u.setId(i);
			u.setActive(1);
			allUsers.add(u);
		}
	}
	
	private void setupAllRoles() {
		allRoles.add(ROLE_USER);
		allRoles.add(ROLE_ADMIN);
	}

	/**
	 * manageUsers() adds all users to model (as "users") and sets the view to
	 * admin/manageUsers.jsp
	 */
	@Test
	public void goToManageUsersTest() {
		mav = ctrl.goToManageUsers(mav);
		assertEquals("Wrong view name", "admin/manageUsers", mav.getViewName());
		Object usersModelObject = mav.getModel().get("users");
		assertNotEquals("allUsers model object was null", null, usersModelObject);
		assertTrue("allUsers model object is wrong type", usersModelObject instanceof Collection<?>);
		Collection<?> users = (Collection<?>) usersModelObject;
		assertEquals("Didn't find all users", users.size(), allUsers.size());
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
	 * If validation succeeds, createUser() inserts the given User object
	 */
	@Test
	public void doCreateUserSuccessTest() throws SesValidationException {
		User u = createUser();
		mav = ctrl.doCreateUser(mav, u, webRequest);

		// Confirm validation passes, saveUser(u) is called, and view is set back to manage users page
		Object failures = mav.getModel().get("failures");
		assertEquals("Validation failed", null, (String) failures);
		verify(userService, times(1)).saveUser(u, webRequest);
		assertEquals("Wrong view name", "admin/manageUsers", mav.getViewName());
	}

	/**
	 * If the user fails validation, createUser() does not persist the object and reports failures
	 * @throws SesValidationException 
	 */
	@Test
	public void doCreateUserFailureTest() throws SesValidationException {
		User u = createUser();
		final String VALIDATION_FAILURE = "VALIDATION_FAILURE";
		SesValidationException vEx = new SesValidationException();
		vEx.addFailure(VALIDATION_FAILURE);
		doThrow(vEx).when(userService).saveUser(u, webRequest); // How vExing, har har...
		mav = ctrl.doCreateUser(mav, u, webRequest);

		// Confirm validation failure
		Object failures = mav.getModel().get("failures");
		assertTrue("Validation didn't fail", ((String) failures).equals(VALIDATION_FAILURE));
		assertEquals("Wrong view name", "admin/createUser", mav.getViewName());
	}

	/**
	 * toggleActive() sets a user to active or inactive (whichever it wasn't before the toggle)
	 */
	@Test
	public void toggleActiveTest() {
		User u = allUsers.get(0);
		assertEquals("Test user did not start active!", 1, u.getActive());
		when(userRepo.findById(u.getId())).thenReturn(u);

		// Attempt to toggle user inactive
		mav = ctrl.toggleActive(mav, u.getId());
		assertEquals("Wrong active status", 0, u.getActive());
		verify(userRepo, times(1)).save(u);
		
		// Confirm return to manage users view
		assertEquals("Wrong view name", "admin/manageUsers", mav.getViewName());
		Collection<?> users = (Collection<?>) mav.getModel().get("users");
		assertEquals("Didn't find all users", users.size(), allUsers.size());

		// Toggle user active again
		mav = ctrl.toggleActive(mav, u.getId());
		assertEquals("Wrong active status", 1, u.getActive());
		verify(userRepo, times(2)).save(u); // Two times because we're counting the earlier verify

		// Confirm return to manage users view
		assertEquals("Wrong view name", "admin/manageUsers", mav.getViewName());
		users = (Collection<?>) mav.getModel().get("users");
		assertEquals("Didn't find all users", users.size(), allUsers.size());
	}

	/**
	 * editUser() adds the User to be modified to the model as "user", adds all roles as "roles", and sets the view to 
	 * admin/editUser.jsp
	 */
	@Test
	public void goToEditUserTest() {
		User u = allUsers.get(0);
		when(userRepo.findById(u.getId())).thenReturn(u);
		
		// Test
		mav = ctrl.goToEditUser(mav, u.getId());
		
		assertEquals("Wrong view name", "admin/editUser", mav.getViewName());
		assertNotEquals(null, mav.getModel().get("user"));
		assertTrue("user is wrong type", mav.getModel().get("user") instanceof User);
		User modelUser = (User) mav.getModel().get("user");
		assertEquals("Model User had wrong ID", u.getId(), modelUser.getId());
		assertEquals("Model User had wrong name", u.getName(), modelUser.getName());
		assertEquals("Model User had wrong email", u.getEmail(), modelUser.getEmail());
		
		// Confirm roles were loaded
		Collection<?> roles = (Collection<?>) mav.getModel().get("allRoles");
		assertEquals("Didn't find all roles", allRoles.size(), roles.size());
	}

	/**
	 * doEditUser() updates the User in the DB, and sets the view back to admin/manageUsers.jsp
	 */
	@Test
	public void doEditUserSuccessTest() throws SesValidationException {
		User u = allUsers.get(0);
		
		// Edit test user's details
		u.setName("NEW_NAME");
		mav = ctrl.doEditUser(mav, webRequest, u);
		
		// Confirm user service saved user, user object is reset, and view is reset to manage users
		verify(userService).saveUser(u, webRequest);
		assertEquals("User model object was not reset", null, mav.getModel().get("user"));
		
		assertEquals("Wrong view name", "admin/manageUsers", mav.getViewName());
		Collection<?> users = (Collection<?>) mav.getModel().get("users");
		assertEquals("Didn't find all users", users.size(), allUsers.size());
	}
	
	/**
	 * doEditUser() fails if the email is changed to one that is already registered with another user
	 */
	@Test
	public void doEditUserFailureTest() throws SesValidationException {
		User u = allUsers.get(0);
		
		// Setup validation failure
		final String VALIDATION_FAILURE = "VALIDATION_FAILURE";
		SesValidationException vEx = new SesValidationException();
		vEx.addFailure(VALIDATION_FAILURE);
		doThrow(vEx).when(userService).saveUser(u, webRequest);
		
		// Edit test user's details
		u.setName("NEW_NAME");
		mav = ctrl.doEditUser(mav, webRequest, u);
		
		// Confirm validation failure
		Object failures = mav.getModel().get("failures");
		assertTrue("Validation didn't fail", ((String) failures).equals(VALIDATION_FAILURE));
		assertEquals("Wrong view name", "admin/editUser", mav.getViewName());
		
		// Confirm user object was not reset, and view was set back to edit user
		verify(userService).saveUser(u, webRequest);
		assertEquals("Wrong view name", "admin/editUser", mav.getViewName());
		Collection<?> roles = (Collection<?>) mav.getModel().get("allRoles");
		assertEquals("Didn't find all roles", allRoles.size(), roles.size());
	}

}
