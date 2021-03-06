package com.fdmgroup.ses.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NavController {	
	
	@RequestMapping(value="/")
    public ModelAndView index() {
		return new ModelAndView("index");
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
