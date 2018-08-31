package com.fdmgroup.ses.validationTest;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.mockito.InjectMocks;

import com.fdmgroup.ses.validation.SesValidationException;
import com.fdmgroup.ses.validation.SesValidator;

public abstract class ValidationTest<T extends SesValidator> {

	@InjectMocks
	protected T validator;
	protected Set<String> failures = new HashSet<>();
	protected Set<String> expectedFailures = new HashSet<>();
	
	/************************************************
	*                 Test Execution                *
	************************************************/
	
	/**
	 * Runs a validation test with the validated object and expected failures having been setup
	 */
	protected void runTest() {
		// Validate and catch failures (if any)
		try {
			validator.validate();
		} catch (SesValidationException ex) {
			failures = ex.getFailures();
		}
		
		// Compare actual and expected failures 
		assertTrue("One or more expected failures did not occur:" + printFailures(),
				failures.containsAll(expectedFailures));
		assertTrue("One or more unexpected failures occured:" + printFailures(),
				expectedFailures.containsAll(failures));
	}
	
	/**
	 * Prints expected and actual failures as strings
	 */
	private String printFailures() {
		return "\nExpected Failures: " + expectedFailures.toString()
				+ "\nActual Failures: " + failures.toString();
	}
	
}
