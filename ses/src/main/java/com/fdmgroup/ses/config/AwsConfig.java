package com.fdmgroup.ses.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Amazon Web Service Simple Email Service SMTP config and credentials
 *
 */
@ConfigurationProperties("ses.awsconfig")
public class AwsConfig {
	
    private String from;
    private String fromName;
    private String smtpUsername;
    private String smtpPassword;
    private String host;
    private int port;

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
