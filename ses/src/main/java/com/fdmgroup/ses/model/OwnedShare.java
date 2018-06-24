package com.fdmgroup.ses.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@SequenceGenerator(name="seq", initialValue=0, allocationSize=1, sequenceName="OWNED_SHARES_SEQUENCE")
@Table(name = "OWNED_SHARES")
public class OwnedShare {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
	@Column(name = "id")
	private int id;
	
	@ManyToOne
	private User owner;

	@ManyToOne
	private Company company;
	
	@Column(name = "quantity")
	private Long quantity = 0l;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "owned_share_id") // TODO, onetoMany is inefficient!
	private List<TransactionHistory> transactionHistory;
	
	/**
	 * Used to control multi-select transactions
	 */
	@Transient
	private Boolean selected;
	
	/**
	 * Used to track the user's average price of purchase for shares of this stock
	 */
	@Transient
	private BigDecimal averagePurchasePrice;
	
	/**
	 * Determines the average price paid for owned shares
	 */
	public BigDecimal getAveragePurchasePrice() {
		if (averagePurchasePrice == null) {
			BigDecimal total = new BigDecimal(0);
			BigDecimal count = new BigDecimal(0);
			for (TransactionHistory txHistory : transactionHistory) {
				if (txHistory.getQuantity() > 0) {
					total = total.add(txHistory.getValue());
					count = count.add(new BigDecimal(txHistory.getQuantity()));
				}
			}		
			averagePurchasePrice = total.divide(count, 2, RoundingMode.HALF_UP);
		}
		return averagePurchasePrice;
	}
	
	public void setAveragePurchasePrice(BigDecimal averagePurchasePrice) {
		this.averagePurchasePrice = averagePurchasePrice;
	}

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

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public List<TransactionHistory> getTransactionHistory() {
		return transactionHistory;
	}

	public void setTransactionHistory(List<TransactionHistory> transactionHistory) {
		this.transactionHistory = transactionHistory;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

}