package com.fdmgroup.ses.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.UserRepository;
import com.fdmgroup.ses.service.UserService;

@Controller
public class AdminController {

	@Autowired
	UserRepository userRepo;

	@Autowired
	UserService userService;
	
	/**
	 * Go to Admin home page
	 */
	@RequestMapping(value="/admin/adminHome")
    public ModelAndView login(ModelAndView modelAndView) {
		modelAndView.setViewName("admin/adminHome");
		return modelAndView;
	}
	
	/**
	 * Go to the user management page
	 */
	@RequestMapping(value="/admin/manageUsers")
    public ModelAndView manageUsers(ModelAndView modelAndView) {
		modelAndView.setViewName("admin/manageUsers");
		modelAndView.addObject("users", userRepo.findAll());
		return modelAndView;
	}
	
	/**
	 * Toggle a user's active/inactive status
	 */
	@RequestMapping(value="/admin/toggleActive")
    public ModelAndView toggleActive(
    		ModelAndView modelAndView,
    		@RequestParam(name="uid") int userId
    ) {
		try {
			User user = userRepo.findById(userId);
			int activeReverseState = user.getActive() == 1 ? 0 : 1;
			user.setActive(activeReverseState);
			userRepo.save(user);
		} catch (Exception e) {
			// TODO: exception handling
			System.out.println("Bad! Exception happened!");
		}
		modelAndView.setViewName("admin/manageUsers");
		modelAndView.addObject("users", userRepo.findAll());
		return modelAndView;
	}
	
	/**
	 * Edit a user
	 */
	@RequestMapping(value="/admin/editUser")
    public ModelAndView editUser(
    		ModelAndView modelAndView,
    		@RequestParam(name="uid") int userId
    ) {
		try {
			User user = userRepo.findById(userId);
			modelAndView.setViewName("admin/editUser");
			modelAndView.addObject("user", user);
		} catch (Exception e) {
			// TODO: exception handling
			System.out.println("Bad! Exception happened!");
		}
		return modelAndView;
	}
	
}
