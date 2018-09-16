package com.fdmgroup.ses.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.email.EmailSender;
import com.fdmgroup.ses.email.ErrorEmail;
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
	
	@Autowired
	private EmailSender emailSender;
	
	/**
	 * Instruct Spring Form how to bind date input
	 * @param binder
	 */
	@InitBinder    
	public void initBinder(WebDataBinder binder){
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	}

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
			modelAndView.setViewName("newRegistrationAdvice");
		} catch (SesValidationException ex) {
			modelAndView.addObject("failures", ValidationUtils.stringifyFailures(ex.getFailures()));
			modelAndView.setViewName("register");
		} catch (Exception e) {
			modelAndView.addObject("failures", e.getMessage());
			modelAndView.setViewName("register");
			emailSender.sendEmail(new ErrorEmail(e));
	    }
		
		return modelAndView;
	}
	
	/**
	 * Confirm email verification and activate user account
	 */
	@RequestMapping(value="/registationConfirm")
    public ModelAndView goToEditUser(
    		ModelAndView modelAndView,
    		@RequestParam(name="token") String token
    ) {
		try {
			userService.activateUser(token);
			modelAndView.addObject("successfulRegistration", true);
		} catch (SesValidationException ex) {
			modelAndView.addObject("failures", ValidationUtils.stringifyFailures(ex.getFailures()));
			modelAndView.addObject("successfulRegistration", false);
		}
		modelAndView.setViewName("login");
		return modelAndView;
	}

}
