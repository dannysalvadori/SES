package com.fdmgroup.ses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import com.fdmgroup.ses.config.QueryConfig;

@SpringBootApplication(scanBasePackages="com.fdmgroup.ses")
@EnableConfigurationProperties(QueryConfig.class)
public class SesApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SesApplication.class, args);		
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SesApplication.class);
    }
	
//	@Bean
//	public JavaMailSender getJavaMailSender() {
//	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//	    mailSender.setHost("smtp.gmail.com");
//	    mailSender.setPort(587);
//	     
//	    mailSender.setUsername("dannysalvadori@gmail.com");
//	    mailSender.setPassword("V-?");
//	     
//	    Properties props = mailSender.getJavaMailProperties();
//	    props.put("mail.transport.protocol", "smtp");
//	    props.put("mail.smtp.auth", "true");
//	    props.put("mail.smtp.starttls.enable", "true");
//	    props.put("mail.debug", "true");
//	     
//	    return mailSender;
//	}
	
}