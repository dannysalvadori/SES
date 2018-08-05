package com.fdmgroup.ses.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.ses.model.Company;

@Repository("companyRepository")
public interface CompanyRepository extends JpaRepository<Company, Long> {
	Company findById(Integer id);
	Company findBySymbol(String symbol);
	List<Company> findBySymbolIn(Set<String> symbols);
}