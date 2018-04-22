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
				.antMatchers("/").permitAll()
				.antMatchers("/user*").hasAnyRole("USER", "ADMIN")
				.antMatchers("/admin*").hasRole("ADMIN") // "/admin*" gets any MAPPING (not end URI) matching /admin*
				// N.B. While this role must be "ADMIN", Spring prepends "ROLE_" - it must be "ROLE_ADMIN" in the DB
			.and()
				.formLogin()
					.and().logout().permitAll();
//				.formLogin()
//					.loginPage("/login") // note this is the default mapping
//					.failureUrl("/login?error") // default mapping
//					.defaultSuccessUrl("/index");
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