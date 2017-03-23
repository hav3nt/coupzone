package com.core.createTables;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * utility class for creating tables in the DB
 * 
 * @author Baruch
 * */
public class CreateDBTables {

	/**
	 * Creates the tables of the coupon system in the DB
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		String driver = "org.apache.derby.jdbc.ClientDriver";
		String DBName = "couponSys";
		String url = "jdbc:derby://localhost:1527/" + DBName + "; create=true";
		Connection connection = null;
		Statement statement = null;

		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url);
			statement = connection.createStatement();
			// Create Company table
			statement
					.execute("CREATE TABLE Company (ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),"
							+ "Comp_Name VARCHAR(25) , Password VARCHAR(25) , Email VARCHAR(25))");
			// Create Customer table
			statement
					.execute("CREATE TABLE Customer (ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),"
							+ "Cust_Name VARCHAR(25) , Password VARCHAR(25))");
			// Create Coupon table
			statement
					.execute("CREATE TABLE Coupon (ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1), "
							+ "Title VARCHAR(25) , Start_Date DATE , End_Date DATE, Amount INT , Type VARCHAR(25) , Message VARCHAR(500) ,"
							+ "Price DOUBLE , Image VARCHAR(45))");
			// Create Customer_Coupon table
			statement
					.execute("CREATE TABLE Customer_Coupon (Cust_ID BIGINT , Coupon_ID BIGINT , "
							+ "PRIMARY KEY (Cust_ID, Coupon_ID))");
			// Create Company_Coupon table
			statement
					.execute("CREATE TABLE Company_Coupon (Comp_ID BIGINT , Coupon_ID BIGINT , "
							+ "PRIMARY KEY (Comp_ID, Coupon_ID))");
		} catch (Exception e) {
			System.out.println("Failed creating tables in the DB");
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("System failure: " + e.getMessage());
			}
		}
	}

}
