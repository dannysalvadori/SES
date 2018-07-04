package com.fdmgroup.ses.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.service.OwnedSharesService;
import com.fdmgroup.ses.service.UserService;
import com.fdmgroup.ses.stockExchange.SaleForm;

@Controller
public class NavController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	OwnedSharesService ownedSharesService;
	
	/**
	 * Go to user home page
	 */
	@RequestMapping(value="/user/myAccount")
    public ModelAndView goToMyAccount(ModelAndView modelAndView) {
		modelAndView.addObject("user", userService.findCurrentUser());
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
		
		return modelAndView;
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
