package com.fdmgroup.ses.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.repository.CompanyRepository;
import com.fdmgroup.ses.service.CreditCardService;
import com.fdmgroup.ses.service.OwnedSharesService;
import com.fdmgroup.ses.service.TransactionService;
import com.fdmgroup.ses.service.UserService;
import com.fdmgroup.ses.stockExchange.SaleForm;
import com.fdmgroup.ses.stockExchange.PurchaseForm;
import com.fdmgroup.ses.validation.SesValidationException;
import com.fdmgroup.ses.validation.ValidatorFactory;
import com.fdmgroup.ses.validation.ValidationUtils;

@Controller
public class StockExchangeController {
	
	@Autowired
	private ValidatorFactory validationFactory;
	
	@Autowired
	private CompanyRepository companyRepo;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private OwnedSharesService ownedSharesService;
	
	@Autowired
	private CreditCardService creditCardService;

	@Autowired
	private UserService userService;
	
	/**
	 * Instruct Spring Form how to bind dates
	 * @param binder
	 */
	@InitBinder    
	public void initBinder(WebDataBinder binder){
		// Date format for expiry date, must include time
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
	}
	
	/* ****************************************************
	 *                     Nav Methods                    *
	 ******************************************************/
	
	/**
	 * Go to Stock Exchange page
	 */
	@RequestMapping(value="/user/stockExchange")
    public ModelAndView goToStockExchange(ModelAndView modelAndView) {
		modelAndView.setViewName("user/stockExchange");
		addStocksToModel(modelAndView);
		return modelAndView;
	}
	
	/**
	 * Adds all SE companies to model in a TransactionForm, as "transactionForm", provided they have at least one stock
	 * available to purchase. The current user's owned shares are added to a SaleForm under "saleForm", again provided
	 * the owned quantity is one or greater.
	 */
	private void addStocksToModel(ModelAndView mav) {
		// Add available stocks for purchase
		PurchaseForm transactionForm = new PurchaseForm();
		List<Company> companies = new ArrayList<>();
		for (Company company : companyRepo.findAll()) {
			if (company.getAvailableShares() > 0) {
				companies.add(company);
			}
		}
		transactionForm.setCompanies(companies);
		mav.addObject("transactionForm", transactionForm);
		
		// Add the user's stocks available to be sold
		SaleForm saleForm = new SaleForm();
		List<OwnedShare> ownedShares = new ArrayList<>();
		for (OwnedShare ownedShare : ownedSharesService.findAllForCurrentUser()) {
			if (ownedShare.getQuantity() > 0) {
				ownedShares.add(ownedShare);
			}
		}
		saleForm.setOwnedShares(ownedShares);
		mav.addObject("saleForm", saleForm);
	}
	
	/* ****************************************************
	 *                   Purchase Methods                 *
	 ******************************************************/
	
	/**
	 * Request to purchase selected stock
	 */
	@RequestMapping(value="/user/doPlaceOrder")
    public ModelAndView doPlaceOrder(
    		ModelAndView modelAndView,
    		@ModelAttribute("transactionForm") PurchaseForm transactionForm
    ) {
		// Refine transaction form
		PurchaseForm purchaseForm = new PurchaseForm();
		for (Company comp : transactionForm.getCompanies()) {
			if (comp.getTransactionQuantity() != null && comp.getTransactionQuantity() > 0) {
				purchaseForm.getCompanies().add(comp);
			}
		}
		
		try {
			// If validation succeeds, override the model's transaction form with the refined form
			validationFactory.getValidator(purchaseForm).validate();
			purchaseForm.initSubmissionDate();
			modelAndView.addObject("transactionForm", purchaseForm);
			modelAndView.addObject("total", purchaseForm.getTransactionValue());
			modelAndView.setViewName("user/confirmPurchase");
			
		} catch (SesValidationException ex) {
			modelAndView.addObject("purchaseFailures", ValidationUtils.stringifyFailures(ex.getFailures()));
			goToStockExchange(modelAndView);
		}
		
		return modelAndView;
	}
	
	/**
	 * Go to authentication page:
	 */
	@RequestMapping(value="/user/authenticatePurchase")
    public ModelAndView goToAuthenticatePurchase(
    		ModelAndView modelAndView,
    		@ModelAttribute("transactionForm") PurchaseForm purchaseForm
    ) {
		modelAndView.setViewName("user/authenticatePurchase");
		purchaseForm.setCreditCards(creditCardService.findAllForCurrentUser());
		modelAndView.addObject("transactionForm", purchaseForm);
		return modelAndView;
	}
	
	/**
	 * Complete a purchase. Assumes payment has already been authorised 
	 */
	@RequestMapping(value="/user/doPurchase")
    public ModelAndView doPurchase(
    		ModelAndView modelAndView,
    		@ModelAttribute("transactionForm") PurchaseForm purchaseForm
    ) {
		try {
			transactionService.buyStocks(userService.findCurrentUser(), purchaseForm);
			modelAndView.setViewName("user/purchaseComplete");
		} catch (SesValidationException ex) {
			modelAndView.addObject("failures", ValidationUtils.stringifyFailures(ex.getFailures()));
			modelAndView.setViewName("user/purchaseFailed");
		}
		return modelAndView;
	}
	
	/* ****************************************************
	 *                     Sale Methods                   *
	 ******************************************************/
	
	/**
	 * Request a purchase on a given stock
	 */
	@RequestMapping(value="/user/goToSellSelected")
    public ModelAndView goToSellSelected(
    		ModelAndView modelAndView,
    		@ModelAttribute("saleForm") SaleForm saleForm
    ) {
		// Refine transaction form
		SaleForm refinedForm = new SaleForm();
		for (OwnedShare share : saleForm.getOwnedShares()) {
			Company comp = share.getCompany();
			if (comp.getTransactionQuantity() != null && comp.getTransactionQuantity() > 0) {
				refinedForm.getOwnedShares().add(share);
			}
		}
		
		try {
			// If validation succeeds, override the model's transaction form with the refined form
			validationFactory.getValidator(refinedForm).validate();
			refinedForm.initSubmissionDate();
			modelAndView.addObject("saleForm", refinedForm);
			modelAndView.addObject("total", refinedForm.getTransactionValue());
			modelAndView.setViewName("user/confirmSale");
		} catch (SesValidationException ex) {
			modelAndView.addObject("saleFailures", ValidationUtils.stringifyFailures(ex.getFailures()));
			goToStockExchange(modelAndView);
		}
		
		return modelAndView;
	}
	
	/**
	 * Complete a purchase. Assumes payment has already been authorised 
	 */
	@RequestMapping(value="/user/doSale")
    public ModelAndView doSale(
    		ModelAndView modelAndView,
    		@ModelAttribute("saleForm") SaleForm saleForm
    ) {
		try {
			transactionService.sellStocks(userService.findCurrentUser(), saleForm);
			modelAndView.setViewName("user/saleComplete");
		} catch (SesValidationException ex) {
			modelAndView.addObject("failures", ValidationUtils.stringifyFailures(ex.getFailures()));
			modelAndView.setViewName("user/saleFailed");
		}
		return modelAndView;
	}
	
}
