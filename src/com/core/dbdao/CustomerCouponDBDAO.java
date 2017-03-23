package com.core.dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.core.dao.CustomerCouponDAO;
import com.core.dblayer.ConnectionPool;

/**
 * A class to handle Java Beans and the DB
 * 
 * @author Baruch
 */
public class CustomerCouponDBDAO implements CustomerCouponDAO {

	private ConnectionPool connectionPool;

	public CustomerCouponDBDAO() throws Exception {
		this.connectionPool = ConnectionPool.getInstance();
	}

	/**
	 * creates new coupon ID for the customer ID, in the Customer_Coupon table
	 * in the DB
	 * 
	 * @param customerID
	 *            - current customer ID
	 * @param couponID
	 *            - the coupon ID
	 * @throws SQLException
	 * */
	@Override
	public void createCustomerCoupon(long customerID, long couponID)
			throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = this.connectionPool.getConnection();
			String sql = "INSERT INTO Customer_Coupon VALUES ( ? , ? )";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, customerID);
			preparedStatement.setLong(2, couponID);
			preparedStatement.executeUpdate();
		} finally {
			this.connectionPool.closeStatement(preparedStatement);
			this.connectionPool.returnConnection(connection);
		}
	}

	/**
	 * returns the coupons IDs of the customer ID, from the Customer_Coupon
	 * table in the DB
	 * 
	 * @param customerID
	 *            - current customer's ID
	 * @throws SQLException
	 * @return a collection of IDs of the current customer's coupons
	 * */
	@Override
	public Collection<Long> readCustomerCouponIDs(long customerID)
			throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Collection<Long> coupons = new ArrayList<Long>();
		try {
			connection = this.connectionPool.getConnection();
			String sql = "SELECT * FROM Customer_Coupon WHERE Cust_ID= ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, customerID);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				coupons.add(resultSet.getLong(2));
			}
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			this.connectionPool.closeStatement(preparedStatement);
			this.connectionPool.returnConnection(connection);
		}
		return coupons;
	}

	/**
	 * checks if a coupon ID belongs to the current customer's ID, in the
	 * Customer_Coupon table in the DB
	 * 
	 * @param customerID
	 *            - current customer's ID
	 * @param couponID
	 *            - coupon ID
	 * @throws SQLException
	 * @return true if the coupon's ID found to belong the customer's ID
	 * */
	@Override
	public boolean isCouponPurchasedByCustomer(long customerID, long couponID)
			throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean check = false;
		try {
			connection = this.connectionPool.getConnection();
			String sql = "SELECT * FROM Customer_Coupon WHERE Cust_ID = ? AND Coupon_ID = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, customerID);
			preparedStatement.setLong(2, couponID);
			resultSet = preparedStatement.executeQuery();
			check = resultSet.next();
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			this.connectionPool.closeStatement(preparedStatement);
			this.connectionPool.returnConnection(connection);
		}
		return check;
	}

	/**
	 * deletes a customer ID from the Customer_Coupon table in the DB
	 * 
	 * @param customerID
	 *            - the customer's ID
	 * @throws SQLException
	 * @return the number of rows who were deleted on the operation
	 * */
	@Override
	public int deleteCustomer(long customerID) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int rowsAffected = 0;
		try {
			connection = this.connectionPool.getConnection();
			String sql = "DELETE FROM Customer_Coupon WHERE Cust_ID = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, customerID);
			rowsAffected = preparedStatement.executeUpdate();
		} finally {
			this.connectionPool.closeStatement(preparedStatement);
			this.connectionPool.returnConnection(connection);
		}
		return rowsAffected;
	}

	/**
	 * deletes a coupon from the Customer_Coupon table in the DB
	 * 
	 * @param couponID
	 *            - the coupon's ID
	 * @throws SQLException
	 * @return the number of rows who were deleted on the operation
	 * */
	@Override
	public int deleteCoupon(long couponID) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int rowsAffected = 0;
		try {
			connection = this.connectionPool.getConnection();
			String sql = "DELETE FROM Customer_Coupon WHERE Coupon_ID = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, couponID);
			rowsAffected = preparedStatement.executeUpdate();
		} finally {
			this.connectionPool.closeStatement(preparedStatement);
			this.connectionPool.returnConnection(connection);
		}
		return rowsAffected;
	}

}
