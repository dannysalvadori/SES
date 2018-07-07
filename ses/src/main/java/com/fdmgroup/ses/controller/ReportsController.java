package com.fdmgroup.ses.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReportsController {	
	
	/**
	 * Go to reports home page
	 */
	@RequestMapping(value="/user/reports")
    public ModelAndView goToReports(ModelAndView modelAndView) {
		modelAndView.setViewName("user/reports");
		return modelAndView;
	}
		
}
