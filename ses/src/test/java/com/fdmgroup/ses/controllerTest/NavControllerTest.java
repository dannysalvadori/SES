package com.fdmgroup.ses.controllerTest;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.controller.NavController;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class NavControllerTest {
	
	@InjectMocks
	private NavController ctrl = new NavController();
	private ModelAndView mav = new ModelAndView();
	
	@Before
	public void setUp() throws Exception {
		//
	}
	
	/**
	 * index() sets the view name to index.jsp
	 */
	@Test
	public void indexTest() {
		mav = ctrl.index();
		assertEquals("Wrong view name", "index", mav.getViewName());
	}
	
	/**
	 * goToAdminHome() sets the view name to admin/adminHome.jsp
	 */
	@Test
	public void goToAdminHomeTest() {
		mav = ctrl.goToAdminHome(mav);
		assertEquals("Wrong view name", "admin/adminHome", mav.getViewName());
	}
	
}
