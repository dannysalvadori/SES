package com.fdmgroup.ses.stockExchange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.CreditCardDetail;

public class TransactionForm {

	private List<Company> companies = new ArrayList<>();
	
	@Transient
	private List<CreditCardDetail> creditCards = new ArrayList<>();

	@Transient
	private Integer creditCardId;
	
	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}
	
	/**
	 * Sums (shareValue * transactionQuantity) for each company in the transaction
	 */
	public BigDecimal getTransactionValue() {
		BigDecimal txValue = new BigDecimal(0);
		for (Company company : companies) {
			txValue = txValue.add(company.getTransactionValue());
		}
		return txValue;
	}

	public List<CreditCardDetail> getCreditCards() {
		return creditCards;
	}

	public void setCreditCards(List<CreditCardDetail> creditCards) {
		this.creditCards = creditCards;
	}

	public Integer getCreditCardId() {
		return creditCardId;
	}

	public void setCreditCardId(Integer cardId) {
		this.creditCardId = cardId;
	}
	
}
