package com.core.dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.core.dao.CompanyCouponDAO;
import com.core.dblayer.ConnectionPool;

/**
 * A class to handle Java Beans and the DB
 * 
 * @author Baruch
 */
public class CompanyCouponDBDAO implements CompanyCouponDAO {

	private ConnectionPool connectionPool;

	public CompanyCouponDBDAO() throws Exception {
		this.connectionPool = ConnectionPool.getInstance();
	}

	/**
	 * creates new coupon ID for the company ID, in the Company_Coupon table in
	 * the DB
	 * @param companyID - the company's ID 
     * @param couponID - the coupon's ID 
	 * @throws SQLException
	 * */
	@Override
	public void createCompanyCoupon(long companyID, long couponID)
			throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = this.connectionPool.getConnection();
			String sql = "INSERT INTO Company_Coupon VALUES ( ? , ?)";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, companyID);
			preparedStatement.setLong(2, couponID);
			preparedStatement.executeUpdate();
		} finally {
			this.connectionPool.closeStatement(preparedStatement);
			this.connectionPool.returnConnection(connection);
		}
	}

	/**
	 * returns the coupons IDs of the company ID, from the Company_Coupon table
	 * in the DB
	 * @param companyID - current company's ID
	 * @throws SQLException
	 * @return a collection of IDs of the current company's coupons
	 * */
	@Override
	public Collection<Long> readCompanyCouponIDs(long companyID)
			throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Collection<Long> coupons = new ArrayList<Long>();
		try {
			connection = this.connectionPool.getConnection();
			String sql = "SELECT * FROM Company_Coupon WHERE Comp_ID= ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, companyID);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				coupons.add(resultSet.getLong(2));
			}
			resultSet.close();
			preparedStatement.close();
		} finally {
			this.connectionPool.closeStatement(preparedStatement);
			this.connectionPool.returnConnection(connection);
		}
		return coupons;
	}

	/**
	 * checks if a coupon ID belongs to the current company's ID, in the
	 * Company_Coupon table in the DB
	 * @param companyID - the ID of the company 
     * @param couponID - the ID of the coupon
	 * @throws SQLException
	 * @return true if coupon's ID found to belong the company's ID
	 * */
	@Override
	public boolean isCouponBelongesToCompany(long companyID, long couponID)
			throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		boolean check = false;
		try {
			connection = this.connectionPool.getConnection();
			String sql = "SELECT * FROM Company_Coupon WHERE Comp_ID = ? AND Coupon_ID = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, companyID);
			preparedStatement.setLong(2, couponID);
			ResultSet resultSet = preparedStatement.executeQuery();
			check = resultSet.next();
		} finally {
			this.connectionPool.closeStatement(preparedStatement);
			this.connectionPool.returnConnection(connection);
		}
		return check;
	}

	/**
	 * deletes a company ID from the Company_Coupon table in the DB
	 * @param companyID - the company's ID 
	 * @throws SQLException
	 * @return the number of rows which were deleted from the table
	 *         Company_Coupon
	 * */
	@Override
	public int deleteCompany(long companyID) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int rowsAffecte = 0;
		try {
			connection = this.connectionPool.getConnection();
			String sql = "DELETE FROM Company_Coupon WHERE Comp_ID = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, companyID);
			rowsAffecte = preparedStatement.executeUpdate();
		} finally {
			this.connectionPool.closeStatement(preparedStatement);
			this.connectionPool.returnConnection(connection);
		}
		return rowsAffecte;
	}

	/**
	 * deletes a coupon from the Company_Coupon table in the DB
	 * @param couponID - the coupon's ID
	 * @throws SQLException
	 * @return the number of rows which were deleted from the table
	 *         Company_Coupon
	 * */
	@Override
	public int deleteCoupon(long couponID) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int rowsAffecte = 0;
		try {
			connection = this.connectionPool.getConnection();
			String sql = "DELETE FROM Company_Coupon WHERE Coupon_ID = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, couponID);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} finally {
			this.connectionPool.closeStatement(preparedStatement);
			this.connectionPool.returnConnection(connection);
		}
		return rowsAffecte;
	}

}
