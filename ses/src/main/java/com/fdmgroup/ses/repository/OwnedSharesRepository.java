package com.fdmgroup.ses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.ses.model.OwnedShare;

@Repository("ownedSharesRepository")
public interface OwnedSharesRepository extends JpaRepository<OwnedShare, Long> {
	OwnedShare findById(int id);
}