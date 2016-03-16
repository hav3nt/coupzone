package com.core.beans;

import java.sql.Date;

import javax.xml.bind.annotation.XmlRootElement;
/**
 * A Java Bean representing Coupon table in DB
 * @author Baruch
 */
@XmlRootElement
public class Coupon {
	private long id;
	private String title;
	private Date startDate;
	private Date endDate;
	private int amount;
	private CouponType type;
	private String message;
	private double price;
	private String image;
	
	public Coupon(){}
	
	public Coupon(String title, Date startDate, Date endDate, int amount,
			CouponType type, String message, double price, String image) {
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.type = type;
		this.message = message;
		this.price = price;
		this.image = image;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStartDate() {
		return startDate.toString();
	}
	public void setStartDate(String startDate) {
		this.startDate = Date.valueOf(startDate);
	}
	public String getEndDate() {
		return endDate.toString();
	}
	public void setEndDate(String endDate) {
		this.endDate = Date.valueOf(endDate);
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getType() {
		return type.toString();
	}
	public void setType(String type) {
		this.type = CouponType.valueOf(type);
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	@Override
	public String toString() {
		return "Coupon id =" + id + ", title =" + title + ", starting date ="
				+ startDate + ", expires =" + endDate + ", amount avalable =" + amount
				+ ", type =" + type + ", description =" + message + ", price ="
				+ price + ", image =" + image ;
	}
	

}
