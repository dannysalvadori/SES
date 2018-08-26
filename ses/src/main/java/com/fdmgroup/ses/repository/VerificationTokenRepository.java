package com.fdmgroup.ses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.ses.model.VerificationToken;

@Repository("verificationTokenRepository")
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
	VerificationToken findByToken(String token);
}
