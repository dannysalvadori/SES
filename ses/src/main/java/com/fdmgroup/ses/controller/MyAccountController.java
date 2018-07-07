package com.fdmgroup.ses.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.model.TransactionHistory;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.RoleRepository;
import com.fdmgroup.ses.repository.TransactionHistoryRepository;
import com.fdmgroup.ses.repository.UserRepository;
import com.fdmgroup.ses.service.OwnedSharesService;
import com.fdmgroup.ses.service.UserService;
import com.fdmgroup.ses.stockExchange.SaleForm;
import com.fdmgroup.ses.validation.SesValidationException;
import com.fdmgroup.ses.validation.ValidationUtils;

@Controller
public class MyAccountController {

	@Autowired
	UserService userService;
	
	@Autowired
	OwnedSharesService ownedSharesService;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@Autowired
	TransactionHistoryRepository txHistoryRepo;
	
	/**
	 * Instruct Spring Form how to bind dates
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
					//From the database 'element' will be a Long
					id = (Integer) element;
				}
				return roleRepo.findById(id);
			}
		});
	}
	
	/**
	 * Go to user home page
	 */
	@RequestMapping(value="/user/myAccount")
    public ModelAndView goToMyAccount(ModelAndView modelAndView) {
		User currentUser = userService.findCurrentUser();
		
		modelAndView.addObject("user", currentUser);
		modelAndView.setViewName("user/myAccount");
		
		// Add the user's stocks available to be sold
		SaleForm saleForm = new SaleForm();
		List<OwnedShare> ownedShares = new ArrayList<>();
		for (OwnedShare ownedShare : ownedSharesService.findAllForCurrentUser()) {
			if (ownedShare.getQuantity() > 0) {
				ownedShares.add(ownedShare);
			}
		}
		saleForm.setOwnedShares(ownedShares);
		modelAndView.addObject("saleForm", saleForm);
		
		// Get user's transaction history
		List<TransactionHistory> userTXHistory = txHistoryRepo.findByOwner(currentUser);
		modelAndView.addObject("userTXHistory", userTXHistory);
		
		return modelAndView;
	}
	
	/**
	 * Go to the user change password page
	 */
	@RequestMapping(value="/user/goToChangePassword")
    public ModelAndView goToChangePassword(ModelAndView modelAndView) {
		modelAndView.setViewName("user/changePassword");
		modelAndView.addObject("user", userService.findCurrentUser());
		return modelAndView;
	}
	
	/**
	 * Go to the edit details page
	 */
	@RequestMapping(value="/user/goToEditDetails")
    public ModelAndView goToEditDetails(ModelAndView modelAndView) {
		modelAndView.setViewName("user/editDetails");
		modelAndView.addObject("user", userService.findCurrentUser());
		return modelAndView;
	}
	
	/**
	 * Change password as supplied in User form. On success, redirect to the "My Account". On failure, reload "Change
	 * Password" reporting errors.
	 * @param user User DTO from password change form 
	 */
	@RequestMapping(value="user/doChangePassword")
    public ModelAndView doChangePassword(
    		ModelAndView modelAndView,
    		@ModelAttribute("user") User user
    ) {
		try {
			userService.saveUser(user);
			modelAndView = goToMyAccount(modelAndView);
		} catch (SesValidationException ex) {
			modelAndView.addObject("failures", ValidationUtils.stringifyFailures(ex.getFailures()));
			modelAndView = goToChangePassword(modelAndView);
		}
		return modelAndView;
	}
	
	/**
	 * Edit user details as supplied in User form. On success, redirect to the "My Account". On failure, reload "Edit
	 * Details" reporting errors.
	 * @param user User DTO from edit details form 
	 */
	@RequestMapping(value="user/doEditDetails")
    public ModelAndView doEditDetails(
    		ModelAndView modelAndView,
    		@ModelAttribute("user") User user
    ) {
		try {
			userService.saveUser(user);
			modelAndView.setViewName("user/myAccount");
		} catch (SesValidationException ex) {
			modelAndView.addObject("failures", ValidationUtils.stringifyFailures(ex.getFailures()));
			modelAndView.setViewName("user/editDetails");
		}
		modelAndView.addObject("user", userService.findCurrentUser());
		return modelAndView;
	}
	
}
