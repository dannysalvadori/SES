package com.fdmgroup.ses.stockExchange;

import java.util.Date;

import com.fdmgroup.ses.utils.DateUtils;

public abstract class TransactionForm {
	
	public static final Integer EXPIRY_MINUTES = 5;
	public static final String EXPIRY_MINUTES_STRING = "five";

	protected Date submissionDate;
	
	public Date getSubmissionDate() {
		return submissionDate;
	}
	
	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}
	
	public void initSubmissionDate() {
		submissionDate = DateUtils.now();
	}
	
}
