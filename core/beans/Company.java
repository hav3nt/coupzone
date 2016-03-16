package com.core.beans;

import java.util.Collection;

import javax.xml.bind.annotation.XmlRootElement;
/**
 * A Java Bean representing Company table in DB
 * @author Baruch
 */
@XmlRootElement
public class Company {
	private long id;
	private String companyName;
	private String password;
	private String email;
	private Collection<Coupon> coupons;
	
	public Company(){}
	
	public Company(String compName,String password,String email) {
		this.companyName=compName;
		this.password=password;
		this.email=email;	
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String compName) {
		this.companyName = compName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Collection<Coupon> getCoupons() {
		return coupons;
	}
	public void setCoupons(Collection<Coupon> coupons) {
		this.coupons = coupons;
	}
	@Override
	public String toString() {
		return "Company id =" + id + ", name =" + companyName + ", password ="
				+ password + ", email =" + email;
	}
	
	

}
