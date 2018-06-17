package com.fdmgroup.ses.service;

import org.springframework.web.context.request.WebRequest;

import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.stockExchange.SaleForm;
import com.fdmgroup.ses.stockExchange.TransactionForm;
import com.fdmgroup.ses.validation.SesValidationException;

public interface UserService {
	public User findCurrentUser();
	public void saveUser(User user, WebRequest request) throws SesValidationException;
	public void createVerificationToken(User user, String token);
	public void deleteUser(User user);
	public void updateCredit(User user, TransactionForm transactionForm);
	public void updateCredit(User user, SaleForm saleForm);
}