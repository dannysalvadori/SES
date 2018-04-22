package com.fdmgroup.ses.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.registration.OnRegistrationCompleteEvent;
import com.fdmgroup.ses.repository.UserRepository;

@Controller
public class RegistrationController {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	ApplicationEventPublisher eventPublisher;

	@RequestMapping(value="/register")
    public ModelAndView register(ModelAndView modelAndView) {
		modelAndView.setViewName("register");
		modelAndView.addObject("newUser", new User());
		return modelAndView;
	}
	
	@RequestMapping(value="/createUser")
    public ModelAndView createUser(
    		ModelAndView modelAndView,
    		@ModelAttribute("newUser") User newUser,
    		WebRequest request
    ) {
		userRepo.save(newUser);
		modelAndView.setViewName("register");
		modelAndView.addObject("success", true);
		
//		try {
			System.out.println("Publishing event...");
	        String appUrl = request.getContextPath();
	        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(newUser, request.getLocale(), appUrl));
//	    } catch (Exception me) {
//	        return new ModelAndView("emailError", "newUser", newUser);
//	    }
		
		return modelAndView;
	}

}
