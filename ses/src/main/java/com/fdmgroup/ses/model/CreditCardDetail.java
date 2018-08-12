package com.fdmgroup.ses.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@SequenceGenerator(name="seq", initialValue=0, allocationSize=1, sequenceName="CREDIT_CARD_SEQUENCE")
@Table(name = "CREDIT_CARD_DETAIL")
public class CreditCardDetail {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "user")
	@NotEmpty(message = "*Please provide the user")
	private User user;
	
	@Column(name = "card_number")
	@NotEmpty(message = "*Please provide the card number. This will be encrypted for security")
	private String cardNumber;

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

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	
}