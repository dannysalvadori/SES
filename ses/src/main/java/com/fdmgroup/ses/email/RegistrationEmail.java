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
    		    "<p>",
    		    "You must veify your email address to complete registration.", 
    		    " To do so, simply click the following link or copy-and-paste it into your browser.</p>", 
    		    "<p><a href='" + confirmationUrl + "'>" + confirmationUrl + "</a></p>",
    		    "<p>This link expires in 24 hours.</p>",
    		    "<br/>",
    		    "<p>Not expecting this email?</p>",
    		    "<p>This is an automated email sent from <a href='http://localhost:3702'>stockSim.com</a> because",
    		    " somebody tried to register an account using " + toAddress + ".",
    		    " If it wasn't you, please ignore this email and accept our apologies for the inconvenience.</p>"
    	);
    }

}
