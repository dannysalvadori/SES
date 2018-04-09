package com.fdmgroup.ses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

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
	
	/**
	 * TODO: Delete this -- Handy debugger to spit out the names of all beans registered
	 */
	@Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//        return args -> {
//            System.out.println("Let's inspect the beans provided by Spring Boot:");
//            String[] beanNames = ctx.getBeanDefinitionNames();
//            Arrays.sort(beanNames);
//            for (String beanName : beanNames) {
//                System.out.println(beanName);
//            }
//        };
		return null;
    }
	
}