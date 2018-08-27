package com.fdmgroup.ses.controllerTest;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.controller.LoginController;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class LoginControllerTest {
	
	@InjectMocks
	private LoginController ctrl = new LoginController();
	private ModelAndView mav = new ModelAndView();
	
	@Before
	public void setUp() throws Exception {
		//
	}
	
	/**
	 * goToLogin() sets the view name to login.jsp
	 */
	@Test
	public void goToLoginTest() {
		mav = ctrl.goToLogin(mav);
		assertEquals("Wrong view name", "login", mav.getViewName());
	}
	
	/**
	 * goToAdminHome() sets the view name to admin/adminHome.jsp
	 */
	@Test
	public void doLoginErrorTest() {
		mav = ctrl.doLoginError(mav);
		assertEquals("Wrong view name", "login", mav.getViewName());
		assertEquals("Wrong error message", "Invalid username or password", mav.getModel().get("error"));
	}
	
	/**
	 * goToAdminHome() sets the view name to admin/adminHome.jsp
	 */
	@Test
	public void doLogoutTest() {
		mav = ctrl.doLogout(mav);
		assertEquals("Wrong view name", "logout-success", mav.getViewName());
	}
	
}
