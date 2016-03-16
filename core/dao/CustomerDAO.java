package com.core.dao;

import java.sql.SQLException;
import java.util.Collection;

import com.core.beans.Customer;

/**
 * A DAO interface for managing Java Beans
 * 
 * @author Baruch
 */
public interface CustomerDAO {
	// inserts new customer ID and company ID
	Long createCustomer(Customer company) throws SQLException;

	// returns a customer with ID like the provided parameter
	Customer readCustomer(long id) throws SQLException;

	// updates a customer
	int updateCustomer(Customer company) throws SQLException;

	// deletes a customer
	int deleteCustomer(Customer company) throws SQLException;

	// returns a customer with name like the provided parameter
	Customer getCustomerByName(String customerName)
			throws SQLException;

	// returns a customer with a name and ID as the provided parameters
	Customer getCustomerByNameAndID(long id, String customerName)
			throws SQLException;

	// returns the customers from DB
	Collection<Customer> getAllCustomers() throws SQLException;

	// returns the ID of the customer with a name and a password like the
	// parameters
	Long login(String customerName, String password)
			throws SQLException;


}
