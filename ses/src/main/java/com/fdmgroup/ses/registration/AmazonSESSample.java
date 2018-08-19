package com.fdmgroup.ses.registration;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AmazonSESSample {

    // This address must be verified
    static final String FROM = "danny.salvadori@gmail.com";
    static final String FROMNAME = "Danny Salvadori";
	
    // Since my AWS SES account is still in the sandbox, this address must also be verified
    static final String TO = "danny.salvadori.fdm@gmail.com";
    
    // My AWS SES SMTP credentials
    static final String SMTP_USERNAME = "AKIAIQ2MI7FOK3W6HWSQ";
    static final String SMTP_PASSWORD = "AlhZh5dvsVjdFvvWmeP6v8JtgTu+p49ogYZr7Rc+JCf4";
    
    // The name of the Configuration Set to use for this message.
    // If you comment out or remove this variable, you will also need to
    // comment out or remove the header below.
//    static final String CONFIGSET = "ConfigSet";
    
    // See http://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html#region-endpoints for more information
    static final String HOST = "email-smtp.eu-west-1.amazonaws.com";
    
    // The port connecting to the Amazon SES SMTP endpoint 
    static final int PORT = 587;
    
    static final String SUBJECT = "Amazon SES test (SMTP interface accessed using Java)";
    
    static final String BODY = String.join(
    	    System.getProperty("line.separator"),
    	    "<h1>Amazon SES SMTP Email Test</h1>",
    	    "<p>This email was sent with HI HI :D Amazon SES using the ", 
    	    "<a href='https://github.com/javaee/javamail'>Javamail Package</a>",
    	    " for <a href='https://www.java.com'>Java</a>."
    	);

    public static void sendEmail() throws Exception {

        // Create a Properties object to contain connection configuration information.
    	Properties props = System.getProperties();
    	props.put("mail.transport.protocol", "smtp");
    	props.put("mail.smtp.port", PORT); 
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.auth", "true");

        // Create a Session object to represent a mail session with the specified properties. 
    	Session session = Session.getDefaultInstance(props);

        // Create a message with the specified information. 
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM,FROMNAME));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
        msg.setSubject(SUBJECT);
        msg.setContent(BODY,"text/html");
        
        // Add a configuration set header. Comment or delete the 
        // next line if you are not using a configuration set
//        msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);
            
        // Create a transport.
        Transport transport = session.getTransport();
                    
        // Send the message.
        try
        {
            System.out.println("Sending...");
            
            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
        	
            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
            System.out.println("Email sent!");
        }
        catch (Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        }
        finally
        {
            // Close and terminate the connection.
            transport.close();
        }
    }
}