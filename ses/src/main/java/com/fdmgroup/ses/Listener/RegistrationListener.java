package com.fdmgroup.ses.Listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import com.fdmgroup.service.UserService;
import com.fdmgroup.service.UserServiceImpl;
import com.fdmgroup.ses.model.User;
import com.fdmgroup.ses.model.VerificationToken;
import com.fdmgroup.ses.registration.OnRegistrationCompleteEvent;
import com.fdmgroup.ses.repository.VerificationTokenRepository;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

	// TODO: make BEANS for these
	private UserService userService = new UserServiceImpl();
	private JavaMailSender mailSender = new JavaMailSenderImpl();
	
	@Autowired
	private MessageSource messages;
	
	@Autowired
	private VerificationTokenRepository vtRepo;
	
	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		System.out.println("onAppEvent -- going to send email");
		this.confirmRegistration(event);
	}
	
	
	private void confirmRegistration(OnRegistrationCompleteEvent event) {
		System.out.println("Confirming reg............");
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		VerificationToken vt = new VerificationToken(user, token);
		vtRepo.save(vt);
		
		String recipientAddress = user.getEmail();
		String subject = "StockSim - Registration Confirmation";
		String confirmationUrl
			= event.getAppUrl() + "/registationConfirm.jsp?token=" + token;
		String message = "Registration success!";//messages.getMessage("message.regSucc", null, event.getLocale());
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText(message + " rn " + "http://localhost:3072" + confirmationUrl);
		System.out.println("About to send email!!");
		mailSender.send(email);
		System.out.println("Sent, apparently");
	}
}