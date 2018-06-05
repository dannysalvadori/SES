package com.fdmgroup.ses.controller;

import java.math.BigDecimal;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.repository.CompanyRepository;
import com.fdmgroup.ses.service.CompanyService;
import com.fdmgroup.ses.stockExchange.TransactionForm;

@Controller
public class StockExchangeController {
	
	@Autowired
	CompanyRepository companyRepo;
	
	@Autowired
	CompanyService stockService;

	/**
	 * Go to Stock Exchange page TODO: move to nav controller. TODO: move "addCompanies" to service
	 */
	@RequestMapping(value="/user/stockExchange")
    public ModelAndView goToAdminHome(ModelAndView modelAndView) {
		modelAndView.setViewName("user/stockExchange");
		addCompaniesToModel(modelAndView);
		return modelAndView;
	}
	
	/**
	 * Request a purchase on a given stock
	 */
	@RequestMapping(value="/user/doPlaceOrder")
    public ModelAndView goToBuyStocks(
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
		modelAndView.addObject("transactionForm", purchaseForm);

		// Total up transaction price
		BigDecimal total = new BigDecimal(0);
		for (Company c : purchaseForm.getCompanies()) {
			BigDecimal subTotal = c.getCurrentShareValue().multiply(new BigDecimal(c.getTransactionQuantity()));
			total = total.add(subTotal);
		}
		modelAndView.addObject("total", total);
		
		
		modelAndView.setViewName("user/confirmPurchase");
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
			
			// TODO: stockService.purchaseStocks();
			modelAndView.setViewName("user/purchaseComplete");
		} catch (ValidationException ex) {
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
