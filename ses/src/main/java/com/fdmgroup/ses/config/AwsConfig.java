package com.fdmgroup.ses.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Amazon Web Service Simple Email Service SMTP config and credentials
 *
 */
@ConfigurationProperties("ses.awsconfig")
public class AwsConfig {
	
    private String from = "danny.salvadori@gmail.com";
    private String fromName = "stockSim Admin";
    private String smtpUsername = "AKIAIQ2MI7FOK3W6HWSQ";
    private String smtpPassword = "AlhZh5dvsVjdFvvWmeP6v8JtgTu+p49ogYZr7Rc+JCf4";
    private String host = "email-smtp.eu-west-1.amazonaws.com";
    private int port = 587;

    public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public String getSmtpUsername() {
		return smtpUsername;
	}
	public void setSmtpUsername(String smtpUsername) {
		this.smtpUsername = smtpUsername;
	}
	public String getSmtpPassword() {
		return smtpPassword;
	}
	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
}
