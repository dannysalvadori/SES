package com.fdmgroup.ses.email;

public abstract class Email {

	protected String subject;
	protected String body;
    protected String toAddress;
    
	public String getSubject() {
		return subject;
	}
	public String getBody() {
		return body;
	}
	public String getToAddress() {
		return toAddress;
	}
	
}
