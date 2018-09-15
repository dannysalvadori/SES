package com.fdmgroup.ses.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.RoleRepository;
import com.fdmgroup.ses.repository.UserRepository;
import com.fdmgroup.ses.service.UserService;
import com.fdmgroup.ses.validation.SesValidationException;
import com.fdmgroup.ses.validation.ValidationUtils;

@Controller
public class AdminCRUDUserController {

	@Autowired
	UserRepository userRepo;

	@Autowired
	UserService userService;
	
	@Autowired
	RoleRepository roleRepo;
	
	/**
	 * Instruct Spring Form how to bind date and role inputs
	 * @param binder
	 */
	@InitBinder    
	public void initBinder(WebDataBinder binder){
		
		// Date format
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
		
		// Interpret roles
		binder.registerCustomEditor(Set.class, new CustomCollectionEditor(Set.class) {
			@Override
			protected Object convertElement(Object element) {
				Integer id = null;
				if (element instanceof String && !((String)element).equals("")) {
					//From the JSP 'element' will be a String
					try {
						id = Integer.parseInt((String) element);
					} catch (NumberFormatException e) {
						// TODO: understand and handle correctly
						System.out.println("Element was " + ((String) element));
						e.printStackTrace();
					}
				} else if (element instanceof Integer) {
					// From the database, 'element' will be a Long
					id = (Integer) element;
				}
				return roleRepo.findById(id);
			}
		});		
	}
	
	/**
	 * Go to the user management page
	 */
	@RequestMapping(value="/admin/manageUsers")
    public ModelAndView goToManageUsers(ModelAndView modelAndView) {
		modelAndView.setViewName("admin/manageUsers");
		modelAndView.addObject("users", userRepo.findAll());
		return modelAndView;
	}
	
	/**
	 * Go to the create new user page
	 */
	@RequestMapping(value="/admin/createUser")
    public ModelAndView goToCreateUser(ModelAndView modelAndView) {
		User newUser = new User();
		modelAndView.setViewName("admin/createUser");
		modelAndView.addObject("newUser", newUser);
		return modelAndView;
	}
	
	/**
	 * Admin registration of a new user. On success, redirect to manage users page. On failure, reload creation page
	 * reporting errors.
	 * @param newUser User DTO from registration form 
	 */
	@RequestMapping(value="admin/doCreateUser")
    public ModelAndView doCreateUser(
    		ModelAndView modelAndView,
    		@ModelAttribute("newUser") User newUser,
    		WebRequest request
    ) {
		try {
			userService.saveUser(newUser, request);
			modelAndView.setViewName("admin/manageUsers");
			modelAndView.addObject("users", userRepo.findAll());
		} catch (SesValidationException ex) {
			modelAndView.addObject("failures", ValidationUtils.stringifyFailures(ex.getFailures()));
			modelAndView.setViewName("admin/createUser");
		}
//	    } catch (Exception me) { // TODO: Catch email failure exceptions
//	        return new ModelAndView("emailError", "newUser", newUser);
//	    }
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
		return goToManageUsers(modelAndView);
	}
	
	/**
	 * Go to edit user page for a given user
	 */
	@RequestMapping(value="/admin/editUser")
    public ModelAndView goToEditUser(
    		ModelAndView modelAndView,
    		@RequestParam(name="uid") int userId
    ) {
		try {
			User user = userRepo.findById(userId);
			modelAndView.setViewName("admin/editUser");
			user.setId(userId);
			modelAndView.addObject("user", user);
			modelAndView.addObject("allRoles", roleRepo.findAll());
		} catch (Exception e) {
			// TODO: exception handling
			System.out.println("Bad! Exception happened!");
		}
		return modelAndView;
	}
	
	/**
	 * Save changes to an edited user
	 */
	@RequestMapping(value="/admin/doEditUser")
    public ModelAndView doEditUser(
    		ModelAndView modelAndView,
    		WebRequest request,
    		@ModelAttribute("user") User user
    ) {
		try {
			userService.saveUser(user, request);
			modelAndView = goToManageUsers(modelAndView);
			modelAndView.addObject("user", null);
		} catch (SesValidationException ex) {
			modelAndView.addObject("failures", ValidationUtils.stringifyFailures(ex.getFailures()));
			modelAndView.addObject("allRoles", roleRepo.findAll());
			modelAndView.setViewName("admin/editUser");
		}
		return modelAndView;
	}
	
}
