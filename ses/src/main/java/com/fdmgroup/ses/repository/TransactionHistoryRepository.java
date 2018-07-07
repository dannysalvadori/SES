package com.fdmgroup.ses.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.ses.model.Company;
import com.fdmgroup.ses.model.TransactionHistory;
import com.fdmgroup.ses.model.User;

@Repository("transactionHistoryRepository")
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
	TransactionHistory findById(int id);
	List<TransactionHistory> findByOwnerAndCompany(User owner, Company company);
	List<TransactionHistory> findByOwner(User owner);
}