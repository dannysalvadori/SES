package com.fdmgroup.ses.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.model.User;

@Repository("ownedSharesRepository")
public interface OwnedSharesRepository extends JpaRepository<OwnedShare, Long> {
	OwnedShare findById(int id);
	List<OwnedShare> findByOwner(User owner);
	OwnedShare findByOwnerAndCompany(User owner, Company company);
	List<OwnedShare> findByOwnerAndCompanySymbolIn(User user, Set<String> symbols);
}