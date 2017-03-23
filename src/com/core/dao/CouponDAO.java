package com.core.dao;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Collection;

import com.core.beans.Coupon;
import com.core.beans.CouponType;

/**
 * A DAO interface for managing Java Beans
 * @author Baruch
 */
public interface CouponDAO {
	// inserts new company ID and company ID
	Long createCoupon(Coupon coupon) throws SQLException;
	// returns a company with ID like the provided parameter
	Coupon readCoupon(long id) throws SQLException;
	// updates a company
	int updateCoupon(Coupon coupon) throws SQLException;
	// deletes a company
	int deleteCoupon(Coupon coupon) throws SQLException;
	// returns a company with name like the provided parameter
	Coupon getCouponByTitle(String title) throws SQLException;
	// returns the companies from DB of type like the provided parameter
	Collection<Coupon> getCouponsByType(CouponType couponType) throws SQLException;
	// returns the companies from DB of price lower than the provided parameter
	Collection<Coupon> getCouponsByTopPrice(double price) throws SQLException;
	// returns the companies from DB of ending date sooner than the provided parameter
	Collection<Coupon> getCouponsByEndDate(Date endDate) throws SQLException;
	// returns the companies from DB
	Collection<Coupon> getAllCoupons() throws SQLException;

}
