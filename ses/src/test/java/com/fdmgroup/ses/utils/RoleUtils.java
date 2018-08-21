package com.fdmgroup.ses.utils;

import com.fdmgroup.ses.model.Role;

public class RoleUtils {

	public static final String ROLE_USER_ROLE = "ROLE_USER";
	public static final String ROLE_ADMIN_ROLE = "ROLE_ADMIN";
	public static final Role ROLE_USER;
	public static final Role ROLE_ADMIN;
	
	static {
		ROLE_USER = new Role();
		ROLE_USER.setRole(ROLE_USER_ROLE);
		ROLE_ADMIN = new Role();
		ROLE_ADMIN.setRole(ROLE_ADMIN_ROLE);
	}
	
}
