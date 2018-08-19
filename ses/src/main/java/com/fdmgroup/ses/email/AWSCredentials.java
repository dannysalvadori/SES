package com.fdmgroup.ses.email;

/**
 * Contains configuration and credentials to connect to the AWS Simple Email Service SMTP
 */
public class AWSCredentials {
	
	// SES SMTP config and credentials
    private static final String FROM = "danny.salvadori@gmail.com";
    private static final String FROMNAME = "stockSim Admin";
    private static final String SMTP_USERNAME = "AKIAIQ2MI7FOK3W6HWSQ";
    private static final String SMTP_PASSWORD = "AlhZh5dvsVjdFvvWmeP6v8JtgTu+p49ogYZr7Rc+JCf4";
    private static final String HOST = "email-smtp.eu-west-1.amazonaws.com";
    private static final int PORT = 587;
    
	public static String getFrom() {
		return FROM;
	}
	public static String getFromName() {
		return FROMNAME;
	}
	public static String getSmtpUsername() {
		return SMTP_USERNAME;
	}
	public static String getSmtpPassword() {
		return SMTP_PASSWORD;
	}
	public static String getHost() {
		return HOST;
	}
	public static int getPort() {
		return PORT;
	}
    
}
