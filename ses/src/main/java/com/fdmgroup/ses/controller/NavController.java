package com.fdmgroup.ses.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.service.UserService;

@Controller
public class NavController {
	
	@Autowired
	UserService userService;
	
	/**
	 * Go to user home page
	 */
	@RequestMapping(value="/user/myAccount")
    public ModelAndView goToMyAccount(ModelAndView modelAndView) {
		modelAndView.addObject("user", userService.findCurrentUser());
		modelAndView.setViewName("user/myAccount");
		return modelAndView;
	}
	
	/**
	 * Go to Admin home page
	 */
	@RequestMapping(value="/admin/adminHome")
    public ModelAndView goToAdminHome(ModelAndView modelAndView) {
		modelAndView.setViewName("admin/adminHome");
		return modelAndView;
	}
		
}
