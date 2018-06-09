package com.fdmgroup.ses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.ses.model.TransactionHistory;

@Repository("transactionHistoryRepository")
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
	TransactionHistory findById(int id);
}