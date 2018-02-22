package com.core.dao;

import java.sql.SQLException;
import java.util.Collection;


/**
 * A DAO interface for managing Java Beans
 * @author Baruch
 */
public interface CustomerCouponDAO {
	// inserts new coupon ID and customer ID
	void createCustomerCoupon(long customerID, long couponID)
			throws SQLException;

	// returns all coupon IDs of the current customer ID
	Collection<Long> readCustomerCouponIDs(long customerID)
			throws SQLException;

	// checks if a coupon ID belongs to the customer ID
	boolean isCouponPurchasedByCustomer(long customerID, long couponID)
			throws SQLException;

	// deletes the customer ID from the table
	int deleteCustomer(long customerID) throws SQLException;

	// deletes the customer ID from the table
	int deleteCoupon(long couponID) throws SQLException;

}
