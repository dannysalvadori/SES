package com.fdmgroup.ses.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.ses.model.CreditCardDetail;
import com.fdmgroup.ses.model.User;

@Repository("creditCardDetailRepository")
public interface CreditCardDetailRepository extends JpaRepository<CreditCardDetail, Long> {
	List<CreditCardDetail> findByUser(User user);
	CreditCardDetail findById(Integer id);
}