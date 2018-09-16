package com.fdmgroup.ses.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.fdmgroup.ses.email.EmailSender;
import com.fdmgroup.ses.email.ErrorEmail;
import com.fdmgroup.ses.email.RegistrationEmail;
import com.fdmgroup.ses.model.VerificationToken;
import com.fdmgroup.ses.registration.OnRegistrationCompleteEvent;
import com.fdmgroup.ses.repository.VerificationTokenRepository;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
	
	@Autowired
	private VerificationTokenRepository vtRepo;
	
	@Autowired
	private EmailSender emailSender;
	
	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}
	
	/**
	 * Sends a verification email to a newly registered user
	 */
	private void confirmRegistration(OnRegistrationCompleteEvent event) {

		// Create verification token, but persist only if email is a success
		VerificationToken vt = new VerificationToken(event.getUser());
		
		// Send email and persist verification token
		try {
			emailSender.sendEmail(new RegistrationEmail(event, vt));
			vtRepo.save(vt);
		} catch (Exception e) {
			System.out.println("Part of the registration process failed.");
			emailSender.sendEmail(new ErrorEmail(e));
		}
	}
}