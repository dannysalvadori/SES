package com.fdmgroup.ses.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
	
	@RequestMapping(value="/login")
    public ModelAndView login(ModelAndView modelAndView) {
		modelAndView.setViewName("login");
		return modelAndView;
	}
	
	/**
	 * Returns to login page with an error message
	 */
	@RequestMapping(value="/login-error")
    public ModelAndView loginError(ModelAndView modelAndView) {
		modelAndView.addObject("error", "Invalid username or password");
		return login(modelAndView);
	}
	
	@RequestMapping(value="/logout-success")
    public ModelAndView logout(ModelAndView modelAndView) {
		modelAndView.setViewName("logout-success");
		return modelAndView;
	}
	
	@RequestMapping(value="/login-success")
    public ModelAndView loginLandingPage(ModelAndView modelAndView) {
		modelAndView.setViewName("user/myAccount");
		return modelAndView;
	}
	
}
