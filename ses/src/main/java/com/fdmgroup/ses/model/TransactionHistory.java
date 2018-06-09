package com.fdmgroup.ses.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@SequenceGenerator(name="seq", initialValue=0, allocationSize=1, sequenceName="TRANSACTION_HISTORY_SEQUENCE")
@Table(name = "TRANSACTION_HISTORY")
public class TransactionHistory {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
	@Column(name = "id")
	private int id;
	
	@ManyToOne
	private User owner;

	@ManyToOne
	private Company company;
	
	@Column(name = "purchase_unit_price")
	private BigDecimal unitPrice;
	
	@Column(name = "purchase_quantity")
	private Long quantity;
	
	@Column(name = "purchase_value")
	private BigDecimal value;
	
	private Date exchangeDate;
	
	@OneToOne
	private OwnedShare ownedShare;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	public Long getQuantity() {
		return quantity;
	}
	
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	public Date getExchangeDate() {
		return exchangeDate;
	}
	
	public void setExchangeDate(Date exchangeDate) {
		this.exchangeDate = exchangeDate;
	}

	public OwnedShare getOwnedShare() {
		return ownedShare;
	}

	public void setOwnedShare(OwnedShare ownedShare) {
		this.ownedShare = ownedShare;
	}

}