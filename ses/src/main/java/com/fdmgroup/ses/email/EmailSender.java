package com.fdmgroup.ses.email;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fdmgroup.ses.config.AwsConfig;

@Component
public class EmailSender {
	
	@Autowired
	private AwsConfig awsConfig;
	
	public void sendEmail(Email email) throws MessagingException, UnsupportedEncodingException  {

        // Create a Properties object to contain connection configuration information.
    	Properties props = System.getProperties();
    	props.put("mail.transport.protocol", "smtp");
    	props.put("mail.smtp.port", awsConfig.getPort());
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.auth", "true");

        // Create a Session object to represent a mail session with the specified properties. 
    	Session session = Session.getDefaultInstance(props);

        // Create a message with the specified information. 
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(awsConfig.getFrom(), awsConfig.getFromName()));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getToAddress()));
        msg.setSubject(email.getSubject());
        msg.setContent(email.getBody(),"text/html");
        
        // Create a transport.
        Transport transport = session.getTransport();
                    
        // Send
        try {
            transport.connect(awsConfig.getHost(), awsConfig.getSmtpUsername(), awsConfig.getSmtpPassword());
            transport.sendMessage(msg, msg.getAllRecipients());
        
        } catch (Exception ex) {
        	// TODO: proper error handling
            System.out.println("AWS email not sent.");
            System.out.println("Error message: " + ex.getMessage());
        
        } finally {
            // Close and terminate the connection.
            transport.close();
        }
    }
	
}
