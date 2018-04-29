package com.fdmgroup.ses.Validation;

import java.util.Set;

public class ValidationUtils {

	/**
	 * Parses a Set of Strings into a single bar (|) delimited String 
	 */
	public static String stringifyFailures(Set<String> failures) {
		return stringifyFailures(failures, "|");
	}
	
	/**
	 * Parses a Set of Strings into a single character delimited String. E.g. Set "[A,B,C]" --> String "A,B,C" 
	 * @param failures
	 * @return
	 */
	public static String stringifyFailures(Set<String> failures, String delimiter) {
		
		String output = "";
		
		// Stringify by delimiter
		for (String failure : failures) {
			output += failure + delimiter;
		}
		
		// Remove trailing delimiter
		int index = output.lastIndexOf(delimiter);
		if(index != -1) {
		    output = output.substring(0,index);
		}
		
		return output;
	}

}