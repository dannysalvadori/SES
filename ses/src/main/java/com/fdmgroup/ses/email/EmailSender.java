package com.fdmgroup.ses.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static com.fdmgroup.ses.email.AWSCredentials.*;

public class EmailSender {

	public static void sendEmail(Email email) throws Exception {

        // Create a Properties object to contain connection configuration information.
    	Properties props = System.getProperties();
    	props.put("mail.transport.protocol", "smtp");
    	props.put("mail.smtp.port", getPort()); 
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.auth", "true");

        // Create a Session object to represent a mail session with the specified properties. 
    	Session session = Session.getDefaultInstance(props);

        // Create a message with the specified information. 
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(getFrom(), getFromName()));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getToAddress()));
        msg.setSubject(email.getSubject());
        msg.setContent(email.getBody(),"text/html");
        
        // Create a transport.
        Transport transport = session.getTransport();
                    
        // Send
        try {
            System.out.println("Sending...");
            
            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(getHost(), getSmtpUsername(), getSmtpPassword());
        	
            transport.sendMessage(msg, msg.getAllRecipients());
            System.out.println("Email sent!");
        
        } catch (Exception ex) {
        	// TODO: proper error handling
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        
        } finally {
            // Close and terminate the connection.
            transport.close();
        }
    }
	
}
