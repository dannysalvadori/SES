package com.fdmgroup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.entity.User;
import com.fdmgroup.repository.UserRepository;

@Controller
public class DemoController {
	
	@Autowired
	private UserRepository userRepo;

	@RequestMapping(value="/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }
	
	@RequestMapping(value="/hello")
    public String sayHi(Model model) {
		User user = new User();
		userRepo.save(user);
		List<User> users = userRepo.findAll();
		model.addAttribute("number", users.size());
        return "hi";
	}
	
	@RequestMapping(value="/exception")
    public ModelAndView throwException(ModelAndView modelAndView) {
		modelAndView.setViewName("hi");
		modelAndView.addObject("number", 7/0);
        return modelAndView;
	}
	
}
