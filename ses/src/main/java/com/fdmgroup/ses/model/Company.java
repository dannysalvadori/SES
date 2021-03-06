package com.fdmgroup.ses.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@SequenceGenerator(name="seq", initialValue=0, allocationSize=1, sequenceName="COMPANY_SEQUENCE")
@Table(
	name = "COMPANIES",
	uniqueConstraints=@UniqueConstraint(columnNames={"symbol"})
)
public class Company {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "symbol")
	@NotEmpty(message = "*Please provide a valid company trading symbol")
	private String symbol;
	
	@Column(name = "name")
	@NotEmpty(message = "*Please provide a company name")
	private String name;
	
	@Column(name = "available_shares")
	private Long availableShares;
	
	@Column(name = "current_value")
	private BigDecimal currentShareValue;
	
	@Column(name = "last_trade")
	private Date lastTrade;
	
	@Column(name = "open")
	private BigDecimal openValue;
	
	@Column(name = "close")
	private BigDecimal closeValue;
	
	@Column(name = "gains")
	private BigDecimal gains = new BigDecimal(0);	
	
	@Transient
	private Long transactionQuantity;
	
	public BigDecimal getTransactionValue() {
		return currentShareValue.multiply(new BigDecimal(transactionQuantity));
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Long getAvailableShares() {
		return availableShares;
	}

	public void setAvailableShares(Long availableShares) {
		this.availableShares = availableShares;
	}

	public BigDecimal getCurrentShareValue() {
		return currentShareValue;
	}

	public void setCurrentShareValue(BigDecimal currentShareValue) {
		this.currentShareValue = currentShareValue;
	}

	public Long getTransactionQuantity() {
		return transactionQuantity;
	}

	public void setTransactionQuantity(Long transactionQuantity) {
		this.transactionQuantity = transactionQuantity;
	}

	public Date getLastTrade() {
		return lastTrade;
	}

	public void setLastTrade(Date lastTrade) {
		this.lastTrade = lastTrade;
	}

	public BigDecimal getOpenValue() {
		return openValue;
	}

	public void setOpenValue(BigDecimal openValue) {
		this.openValue = openValue;
	}

	public BigDecimal getCloseValue() {
		return closeValue;
	}

	public void setCloseValue(BigDecimal closeValue) {
		this.closeValue = closeValue;
	}

	public BigDecimal getGains() {
		return gains;
	}

	public void setGains(BigDecimal gains) {
		this.gains = gains;
	}

}