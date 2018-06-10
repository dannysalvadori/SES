package com.fdmgroup.ses.stockExchange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fdmgroup.ses.model.OwnedShare;

public class SaleForm {

	List<OwnedShare> ownedShares;

	public List<OwnedShare> getOwnedShares() {
		if (ownedShares == null) {
			ownedShares = new ArrayList<>();
		}
		return ownedShares;
	}

	public void setOwnedShares(List<OwnedShare> ownedShares) {
		this.ownedShares = ownedShares;
	}
	
	/**
	 * Sums (shareValue * transactionQuantity) for each company in the transaction
	 */
	public BigDecimal getTransactionValue() {
		BigDecimal txValue = new BigDecimal(0);
//		for (Company company : companies) {
//			txValue = txValue.add(company.getTransactionValue());
//		}
		return txValue;
	}
	
}
