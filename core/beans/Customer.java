package com.core.beans;

import java.util.Collection;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A Java Bean representing Customer table in DB
 * @author Baruch
 */
@XmlRootElement
public class Customer {
	private long id;
	private String customerName;
	private String password;
	private Collection<Coupon> coupons;
	
	public Customer(){}
	
	public Customer(String custName,String password) {
		this.customerName=custName;
		this.password=password;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String custName) {
		this.customerName = custName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Collection<Coupon> getCoupons() {
		return coupons;
	}
	public void setCoupons(Collection<Coupon> coupons) {
		this.coupons = coupons;
	}
	@Override
	public String toString() {
		return "Customer id =" + id + ", name =" + customerName + ", password ="
				+ password;
	}
	

}
