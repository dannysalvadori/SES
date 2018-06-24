package com.fdmgroup.ses.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DataSource dataSource;
	
	@Autowired // Defines user/password/role queries as custom properties in application.properties
	QueryConfig queryConfig;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/user*", "/user/**").hasAnyRole("USER", "ADMIN")
				.antMatchers("/admin*", "/admin/**").hasRole("ADMIN")
				// "/admin*" gets any MAPPING (not end URI) matching /admin___; /admin/** gets anything mapped admin/___
				// N.B. While this role must be "ADMIN", Spring prepends "ROLE_" - it must be "ROLE_ADMIN" in the DB
			.and()
				.formLogin()
					.loginPage("/login") // Without this, Boot's own default page is used
					.failureUrl("/login-error") // default mapping
					.defaultSuccessUrl("/login-success")
					.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/logout-success");
	}
	
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.jdbcAuthentication()
			.dataSource(dataSource)
				.usersByUsernameQuery(queryConfig.getUserQuery())
				.authoritiesByUsernameQuery(queryConfig.getRoleQuery());
	}	
	
}