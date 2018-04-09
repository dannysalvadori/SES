package com.fdmgroup.ses.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/css/**", "/", "/index", "/login**").permitAll()
				.antMatchers("/views/admin/**", "admin/**", "/adminHome", "/admin", "/admin*").hasRole("ADMIN") // "/admin*" gets any MAPPING (not end URI) matching /admin*
			.and()
				.formLogin()
					.and().logout().permitAll();
//				.formLogin()
//					.loginPage("/login") // use to setup custom login page
//					.defaultSuccessUrl("/index")
//					.failureUrl("/login?error=true");
	}

	@Autowired
	DataSource dataSource;
	
	@Autowired // Defines user/password/role queries as custom properties in application.properties
	QueryConfig queryConfig;
	
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.jdbcAuthentication()
			.dataSource(dataSource)
				.usersByUsernameQuery(queryConfig.getUserQuery())
				.authoritiesByUsernameQuery(queryConfig.getRoleQuery());
	}	
	
}