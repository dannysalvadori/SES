package com.fdmgroup.ses.validation;

import java.util.HashSet;
import java.util.Set;

public abstract class ModelValidator implements SesValidator {
	
	public Set<String> failures = new HashSet<String>();

	/**
	 * Throw SesValidationException if any validation failures were captured
	 * @throws SesValidationException
	 */
	public void throwFailures() throws SesValidationException {
		if (!failures.isEmpty()) {
			throw new SesValidationException(failures);
		}
	}

}
