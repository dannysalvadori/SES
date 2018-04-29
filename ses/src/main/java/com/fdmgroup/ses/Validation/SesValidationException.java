package com.fdmgroup.ses.Validation;

import java.util.Set;
import java.util.HashSet;

public class SesValidationException extends Exception {
	
	private static final long serialVersionUID = 7688262753725460105L;
	
	private Set<String> failures;

	public SesValidationException() {
		this.failures = new HashSet<String>();
	}
	
	public SesValidationException(Set<String> failures) {
		this.failures = failures;
	}
	
	public void addFailure(String failureMessage) {
		failures.add(failureMessage);
	}

	public Set<String> getFailures() {
		return failures;
	}

	public void setFailures(Set<String> failures) {
		this.failures = failures;
	}
	
}
