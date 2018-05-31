package com.fdmgroup.ses.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
	
	/**
	 * Go to login page
	 */
	@RequestMapping(value="/login")
    public ModelAndView goToLogin(ModelAndView modelAndView) {
		modelAndView.setViewName("login");
		return modelAndView;
	}
	
	/**
	 * Returns to login page with an error message
	 */
	@RequestMapping(value="/login-error")
    public ModelAndView doLoginError(ModelAndView modelAndView) {
		modelAndView.setViewName("login");
		modelAndView.addObject("error", "Invalid username or password");
		return goToLogin(modelAndView);
	}
	
	/**
	 * Go to logout success page
	 */
	@RequestMapping(value="/logout-success")
    public ModelAndView doLogout(ModelAndView modelAndView) {
		modelAndView.setViewName("logout-success");
		return modelAndView;
	}
	
	/**
	 * Go to log in landing page (My Account)
	 */
	@RequestMapping(value="/login-success")
    public ModelAndView goToLoginLandingPage(ModelAndView modelAndView) {
		modelAndView.setViewName("user/myAccount");
		return modelAndView;
	}
	
}
