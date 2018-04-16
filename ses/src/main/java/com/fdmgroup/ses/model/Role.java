package com.fdmgroup.ses.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ROLES")
@SequenceGenerator(name="seq", initialValue=0, allocationSize=1, sequenceName="ROLE_SEQUENCE")
public class Role {
//@Entity
//@Table(name="ROLES")
//@SequenceGenerator(name="seq", initialValue=0, allocationSize=1, sequenceName="ROLE_SEQUENCE")
//public class Role {
//		@Id
//		@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
//		private Long Id;
	@Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
	@Column(name="id")
	private int id;
	
	@Column(name="role")
	private String role;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	
}