package com.fdmgroup.ses.controllerTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static com.fdmgroup.ses.utils.UserUtils.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.controller.RegistrationController;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.RoleRepository;
import com.fdmgroup.ses.repository.UserRepository;
import com.fdmgroup.ses.service.UserService;
import com.fdmgroup.ses.validation.SesValidationException;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class RegistrationControllerTest {
	
	@InjectMocks
	private RegistrationController ctrl = new RegistrationController();
	
	@Mock
	private RoleRepository roleRepo;
	@Mock
	private UserRepository userRepo;
	@Mock
	private UserService userService;
	@Mock
	private WebRequest webRequest;
	
	private ModelAndView mav = new ModelAndView();
	
	@Before
	public void setUp() throws Exception {

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
	 * If validation succeeds, createUser() saves the given User object
	 */
	@Test
	public void createUserSuccessTest() throws SesValidationException {
		User testUser = createUser();
		mav = ctrl.createUser(mav, testUser, webRequest);
		
		// Confirm validation passes
		Object failures = mav.getModel().get("failures");
		assertEquals("Validation failed", null, (String)failures);
		
		verify(userService, times(1)).saveUser(testUser, webRequest);
	}
	
	/**
	 * If user validation fails, createUser() must not save the given User object.
	 */
	@Test
	public void createUserValidationFailureTest() throws SesValidationException {
		
		User testUser = createUser();
		Mockito.doThrow(new SesValidationException()).when(userService).saveUser(testUser, webRequest);		
		
		testUser.setConfirmationPassword("Something different");
		mav = ctrl.createUser(mav, testUser, webRequest);
		
		// Confirm validation failure
		Object failures = mav.getModel().get("failures");
		assertTrue("Validation didn't fail", "" == (String)failures);
		
		verify(userService, times(0)).saveUser(testUser);
	}
	
	/**
	 * goToEditUser(ModelAndView, String) activates the user and sets view to login page with a success message
	 */
	@Test
	public void goToEditUserTest() throws SesValidationException {
		String tokenId = "tokenId";
		mav = ctrl.goToEditUser(mav, tokenId);
		assertEquals("Wrong view name", "login", mav.getViewName());
		assertEquals("Wrong view name", true, mav.getModel().get("successfulRegistration"));
		verify(userService, times(1)).activateUser(tokenId);
	}

}
