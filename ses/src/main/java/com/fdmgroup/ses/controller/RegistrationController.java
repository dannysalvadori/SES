package com.fdmgroup.ses.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.service.UserService;
import com.fdmgroup.ses.validation.SesValidationException;
import com.fdmgroup.ses.validation.ValidationUtils;

@Controller
public class RegistrationController {

	@Autowired
	private UserService userService;
	
	@Autowired
	ApplicationEventPublisher eventPublisher;

	/**
	 * Go to registration page
	 */
	@RequestMapping(value="/register")
    public ModelAndView register(ModelAndView modelAndView) {
		modelAndView.setViewName("register");
		modelAndView.addObject("newUser", new User());
		return modelAndView;
	}
	
	/**
	 * Register a new user. On success, redirect to login. On failure, reload registration page reporting errors.
	 * @param newUser User DTO from registration form 
	 */
	@RequestMapping(value="/registerUser")
    public ModelAndView createUser(
    		ModelAndView modelAndView,
    		@ModelAttribute("newUser") User newUser,
    		WebRequest request
    ) {
		try {
			userService.saveUser(newUser, request);
			modelAndView.addObject("successfulRegistration", true);
			modelAndView.setViewName("login");
		} catch (SesValidationException ex) {
			modelAndView.addObject("failures", ValidationUtils.stringifyFailures(ex.getFailures()));
			modelAndView.setViewName("register");
		}
//	    } catch (Exception me) { // TODO: Catch email failure exceptions
//	        return new ModelAndView("emailError", "newUser", newUser);
//	    }
		
		return modelAndView;
	}

}
