package com.core.dao;

import java.sql.SQLException;
import java.util.Collection;

import com.core.beans.Company;

/**
 * A DAO interface for managing Java Beans
 * @author Baruch
 */
public interface CompanyDAO {

	// inserts new company ID and company ID
	Long createCompany(Company company) throws SQLException;

	// returns a company with ID like the provided parameter
	Company readCompany(long id) throws SQLException;

	// updates a company
	int updateCompany(Company company) throws SQLException;

	// deletes a company
	int deleteCompany(Company company) throws SQLException;

	// returns a company with name like the provided parameter
	Company getCompanyByName(String companyName) throws SQLException;

	// returns a company with ID and name like the provided parameters
	Company getCompanyByNameAndID(long id, String compName)
			throws SQLException;

	// returns the companies from DB
	Collection<Company> getAllCompanies() throws SQLException;

	// returns an ID of the founded company with name and password like the
	// parameters
	Long login(String companyName, String password) throws SQLException;

}
