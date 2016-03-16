package com.core.dao;

import java.sql.SQLException;
import java.util.Collection;


/**
 * A DAO interface for managing Java Beans
 * @author Baruch
 */
public interface CompanyCouponDAO {

	// inserts new coupon ID and company ID
	void createCompanyCoupon(long companyID, long couponID)
			throws SQLException;

	// returns all coupon IDs of the current company ID
	Collection<Long> readCompanyCouponIDs(long companyID)
			throws SQLException;

	// checks if a coupon ID belongs to the company ID
	boolean isCouponBelongesToCompany(long companyID, long couponID)
			throws SQLException;

	// deletes the company ID from the table
	int deleteCompany(long companyID) throws SQLException;

	// deletes the coupon ID from the table
	int deleteCoupon(long couponID) throws SQLException;

	
}
