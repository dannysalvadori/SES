package com.fdmgroup.ses.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("ses.queries")
public class QueryConfig {
	
	/**
     * User query
     */
    private String userQuery;
    
    /**
     * Role query
     */
    private String roleQuery;

	public String getUserQuery() {
		return userQuery;
	}

	public void setUserQuery(String userQuery) {
		this.userQuery = userQuery;
	}

	public String getRoleQuery() {
		return roleQuery;
	}

	public void setRoleQuery(String roleQuery) {
		this.roleQuery = roleQuery;
	}
	
}
