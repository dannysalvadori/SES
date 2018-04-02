package com.fdmgroup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DemoController {

	@RequestMapping(value="/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }
	
	@RequestMapping(value="/hello")
    public String sayHi(Model model) {
		model.addAttribute("number", 7);
        return "hi";
	}
	
	@RequestMapping(value="/exception")
    public ModelAndView throwException(ModelAndView modelAndView) {
		modelAndView.setViewName("hi");
		modelAndView.addObject("number", 7/0);
        return modelAndView;
	}
	
}
