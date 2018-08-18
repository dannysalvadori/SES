package com.fdmgroup.ses.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@SequenceGenerator(name = "seq", initialValue = 0, allocationSize = 1, sequenceName = "CREDIT_CARD_SEQUENCE")
@Table(name = "CREDIT_CARD_DETAIL")
public class CreditCardDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	@Column(name = "id")
	private Integer id;

	/**
	 * The user who is registering this card
	 */
	@ManyToOne
	private User user;

	/**
	 * Distinct to user; the card holder's name as printed on the card (may be different to the user)
	 */
	@Column(name = "card_holder")
	private String cardHolderName;

	@Column(name = "card_number")
	private String cardNumber;
	
	@Column(name = "card_signature")
	private String cardSignature;

	private Date expiryDate;
	
	@Column()
	private int expired = 0; // 0 = FALSE, anything else = TRUE

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getCardHolderName() {
		return cardHolderName;
	}
	
	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public int getExpired() {
		return expired;
	}

	public void setExpired(int expired) {
		this.expired = expired;
	}

	public String getCardSignature() {
		return cardSignature;
	}

	public void setCardSignature(String cardSignature) {
		this.cardSignature = cardSignature;
	}

}