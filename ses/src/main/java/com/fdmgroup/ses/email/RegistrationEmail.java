package com.fdmgroup.ses.email;

import com.fdmgroup.ses.model.VerificationToken;
import com.fdmgroup.ses.registration.OnRegistrationCompleteEvent;

public class RegistrationEmail extends Email {
    
    /**
     * Generate email with the new user's email address and verification token included
     */
    public RegistrationEmail(OnRegistrationCompleteEvent event, VerificationToken vt) {
    	
    	subject = "StockSim Registration";
    	toAddress = event.getUser().getEmail();
    	
    	String confirmationUrl = "http://localhost:3702" + event.getAppUrl() + "/registationConfirm?token=" + vt.getToken();
    	body = String.join(
    		    System.getProperty("line.separator"),
    		    "<h1>Registration is not complete!</h1>",
    		    "<p>You must veify your email address to complete registration. ", 
    		    "You will not be able to log in until you do. ", 
    		    "Verify your account by clicking the following link, or copy-pasting it into your browser. ", 
    		    "<a href='" + confirmationUrl + "'>" + confirmationUrl + "</a>",
    		    "This link will expire in 24 hours. ",
    		    "This email was sent to " + toAddress + " because it was submitted for registeration on stockSim.com. ",
    		    "If this wasn't you, just ignore this email.",
    		    "</p>."
    	);
    }

}
