package com.fdmgroup.ses.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.repository.CompanyRepository;
import com.fdmgroup.ses.repository.UserRepository;
import com.fdmgroup.ses.service.TransactionService;
import com.fdmgroup.ses.stockExchange.TransactionForm;
import com.fdmgroup.ses.validation.SesValidationException;
import com.fdmgroup.ses.validation.ValidationFactory;
import com.fdmgroup.ses.validation.ValidationUtils;

@Controller
public class StockExchangeController {
	
	@Autowired
	ValidationFactory validationFactory;
	
	@Autowired
	CompanyRepository companyRepo;
	
	@Autowired
	TransactionService transactionService;

	@Autowired
	UserRepository userRepo;

	/**
	 * Go to Stock Exchange page TODO: move to nav controller. TODO: move "addCompanies" to service
	 */
	@RequestMapping(value="/user/stockExchange")
    public ModelAndView goToStockExchange(ModelAndView modelAndView) {
		modelAndView.setViewName("user/stockExchange");
		addCompaniesToModel(modelAndView);
		return modelAndView;
	}
	
	/**
	 * Request a purchase on a given stock
	 */
	@RequestMapping(value="/user/doPlaceOrder")
    public ModelAndView doPlaceOrder(
    		ModelAndView modelAndView,
    		@ModelAttribute("transactionForm") TransactionForm transactionForm
    ) {
		// Refine transaction form
		TransactionForm purchaseForm = new TransactionForm();
		for (Company comp : transactionForm.getCompanies()) {
			if (comp.getSelected() != null && comp.getSelected() == true
					&& comp.getTransactionQuantity() != null && comp.getTransactionQuantity() > 0) {
				purchaseForm.getCompanies().add(comp);
			}
		}
		
		try {
			// If validation succeeds, override the model's transaction form with the refined form
			validationFactory.getValidator(purchaseForm).validate();
			modelAndView.addObject("transactionForm", purchaseForm);
			modelAndView.addObject("total", purchaseForm.getTransactionValue());
			modelAndView.setViewName("user/confirmPurchase");
			
		} catch (SesValidationException ex) {
			modelAndView.addObject("failures", ValidationUtils.stringifyFailures(ex.getFailures()));
			goToStockExchange(modelAndView);
		}
		
		return modelAndView;
	}
	
	/**
	 * Go to authentication page:
	 * - TODO: Apply Broker validations
	 * - TODO: Make user Provide credit card details 
	 */
	@RequestMapping(value="/user/authenticatePurchase")
    public ModelAndView goToAuthenticatePurchase(
    		ModelAndView modelAndView,
    		@ModelAttribute("transactionForm") TransactionForm purchaseForm
    ) {
		modelAndView.setViewName("user/authenticatePurchase");
		modelAndView.addObject("transactionForm", purchaseForm);
		return modelAndView;
	}
	
	/**
	 * Complete a purchase. Assumes payment has already been authorised 
	 */
	@RequestMapping(value="/user/doPurchase")
    public ModelAndView doPurchase(
    		ModelAndView modelAndView,
    		@ModelAttribute("transactionForm") TransactionForm purchaseForm
    ) {
		try {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User user = userRepo.findByEmail(userDetails.getUsername());
			transactionService.buyStocks(user, purchaseForm);
			modelAndView.setViewName("user/purchaseComplete");
		} catch (SesValidationException ex) {
			modelAndView.addObject("failures", ValidationUtils.stringifyFailures(ex.getFailures()));
			modelAndView.setViewName("user/purchaseFailed");
		}
		return modelAndView;
	}
	
	/**
	 * Adds all SE companies to model in a TransactionForm, as "transactionForm"
	 */
	private void addCompaniesToModel(ModelAndView mav) {
		TransactionForm transactionForm = new TransactionForm();
		transactionForm.setCompanies(companyRepo.findAll());
		mav.addObject("transactionForm", transactionForm);
	}
	
}
