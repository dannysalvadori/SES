package com.fdmgroup.ses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.ses.model.Role;

@Repository("roleRepository")
public interface RoleRepository extends JpaRepository<Role, Long>{
	Role findByRole(String role);
	Role findById(Integer id);
}
