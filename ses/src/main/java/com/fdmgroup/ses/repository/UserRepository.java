package com.fdmgroup.ses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.ses.model.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
	User findById(int id);
	User findByEmail(String email);
}