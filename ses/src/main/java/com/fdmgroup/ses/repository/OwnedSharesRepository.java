package com.fdmgroup.ses.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.model.User;

@Repository("ownedSharesRepository")
public interface OwnedSharesRepository extends JpaRepository<OwnedShare, Long> {
	OwnedShare findById(int id);
	List<OwnedShare> findByOwner(User owner);
}