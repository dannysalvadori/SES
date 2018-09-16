package com.fdmgroup.ses.email;

import java.util.Arrays;
import java.util.GregorianCalendar;

public class ErrorEmail extends Email {
    
	private final String ADMIN_ADDRESS = "danny.salvadori.fdm@gmail.com";
	
    /**
     * Generate email with the new user's email address and verification token included
     */
    public ErrorEmail(Exception e) {
    	
    	subject = "stockSim Error Log";
    	toAddress = ADMIN_ADDRESS;
    	
    	body = String.join(
    		    System.getProperty("line.separator"),
    		    "<h1>Error Log</h1>",
    		    "<table>",
    		    "<tr><td>Time</td><td>" + GregorianCalendar.getInstance().getTime() + "</td></tr>",
    		    "<tr><td>Class</td><td>" + e.getClass() + "</td></tr>",
    		    "<tr><td>Message</td><td>" + e.getMessage() + "</td></tr>",
    		    "<tr><td>Stack Trace</td><td>" + Arrays.toString(e.getStackTrace()) + "</td></tr>",
    		    "</table>"
    	);
    }

}
