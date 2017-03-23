package com.core.facades;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.core.beans.Company;
import com.core.beans.Customer;
import com.core.dbdao.CompanyCouponDBDAO;
import com.core.dbdao.CompanyDBDAO;
import com.core.dbdao.CouponDBDAO;
import com.core.dbdao.CustomerCouponDBDAO;
import com.core.dbdao.CustomerDBDAO;
import com.core.exceptions.CouponSystemException;

/**
 * A facade class of an Admin client
 */
public class AdminFacade implements ClientFacade {

	private CompanyDBDAO companyDAO;
	private CustomerDBDAO customerDAO;
	private CouponDBDAO couponDAO;
	private CompanyCouponDBDAO compCoupDAO;
	private CustomerCouponDBDAO custCoupDAO;

	public AdminFacade() throws CouponSystemException {
		try {
			companyDAO = new CompanyDBDAO();
			customerDAO = new CustomerDBDAO();
			couponDAO = new CouponDBDAO();
			compCoupDAO = new CompanyCouponDBDAO();
			custCoupDAO = new CustomerCouponDBDAO();
		} catch (Exception e) {
			throw new CouponSystemException("Failed loading system", e);
		}
	}

	/**
	 * adds a company into the DB
	 * 
	 * @param company
	 *            - the company instance to add into DB
	 * @throws CouponSystemException
	 *             in case a company with the same name already exists on DB
	 * @return the ID of the created company, or null if coulden't retrieve the
	 *         ID
	 */
	public Long createCompany(Company company) throws CouponSystemException {
		Long returnedID = null;
		try {
			if (companyDAO.getCompanyByName(company.getCompanyName()) != null) {
				throw new CouponSystemException(
						"Company with the name '" + company.getCompanyName() + "' already exists");
			}
			returnedID = companyDAO.createCompany(company);
		} catch (SQLException e) {
			throw new CouponSystemException("Failed creating company", e);
		}
		return returnedID;
	}

	/**
	 * returns a company from DB by the provided id
	 * 
	 * @param id - the id of the searched company
	 * @throws CouponSystemException
	 *             in case id doesn't exists on DB
	 * @return a Company instance
	 */
	public Company readCompany(long id) throws CouponSystemException {
		Company company = null;
		try {
			company = companyDAO.readCompany(id);
		} catch (SQLException e1) {
			throw new CouponSystemException("Failed reading company", e1);
		}
		if (company == null) {
			throw new CouponSystemException("Company with id: " + id + " wasn't found");
		}
		return company;
	}

	/**
	 * updates the provided company details on DB
	 * 
	 * @param company
	 *            - the company instance to update
	 * @throws CouponSystemException
	 *             in case company with id and name doesn't exist on DB, which
	 *             might indicate that the user tried to change the company
	 *             name, which is not allowed
	 * @return the number of rows affected in the tables on DB
	 */
	public int updateCompany(Company company) throws CouponSystemException {
		try {
			if (companyDAO.getCompanyByNameAndID(company.getId(), company.getCompanyName()) == null) {
				throw new CouponSystemException("Company with id '" + company.getId() + "' and name '"
						+ company.getCompanyName() + "' doesn't exist");
			}
			return companyDAO.updateCompany(company);
		} catch (SQLException e1) {
			throw new CouponSystemException("Failed updating company", e1);
		}
	}

	/**
	 * deletes a company from the DB and it's coupons
	 * 
	 * @param company
	 *            - the company instance to be deleted from the DB
	 * @throws CouponSystemException
	 * @return the number of rows affected from the delete company operation in the tables on DB
	 */
	public int deleteCompany(Company company) throws CouponSystemException {
		int rowsAffected=0;
		try {
			if (companyDAO.readCompany(company.getId()) == null) {
				throw new CouponSystemException("Company with id '" + company.getId() + "wasn't found");
			}
			Collection<Long> couponsIDs = compCoupDAO.readCompanyCouponIDs(company.getId());
			for (Long id : couponsIDs) {
				custCoupDAO.deleteCoupon(id);
				couponDAO.deleteCoupon(couponDAO.readCoupon(id));
			}
			rowsAffected+=companyDAO.deleteCompany(company);
			rowsAffected+=compCoupDAO.deleteCompany(company.getId());
		} catch (SQLException e1) {
			throw new CouponSystemException("Failed deleting company", e1);
		}
		return rowsAffected;
	}

	/**
	 * returns a collection of all the companies registered on DB
	 * @throws CouponSystemException
	 * @return collection of type Company
	 */
	public Collection<Company> getAllCompanies() throws CouponSystemException {
		Collection<Company> companies = new ArrayList<Company>();
		try {
			companies = companyDAO.getAllCompanies();
		} catch (SQLException e1) {
			throw new CouponSystemException("Failed reading companies", e1);
		}
		return companies;
	}

	/**
	 * adds a customer into DB
	 * 
	 * @param customer
	 *            - the customer instance to add into DB
	 * @throws CouponSystemException
	 *             in case a customer with the same name already exists on DB
	 * @return the ID of the created customer
	 */
	public Long createCustomer(Customer customer) throws CouponSystemException {
		Long returnedID = null;
		try {
			if (customerDAO.getCustomerByName(customer.getCustomerName()) != null) {
				throw new CouponSystemException(
						"Customer with the name '" + customer.getCustomerName() + "' already exists on DB");
			}
			returnedID = customerDAO.createCustomer(customer);
		} catch (SQLException e1) {
			throw new CouponSystemException("Failed creating customer", e1);
		}
		return returnedID;
	}

	/**
	 * returns a customer from DB by the provided id
	 * 
	 * @param id - the id of the desired customer
	 * @throws CouponSystemException
	 *             in case id doesn't exists on DB
	 * @return a Customer instance
	 */
	public Customer readCustomer(long id) throws CouponSystemException {
		Customer customer = null;
		try {
			customer = customerDAO.readCustomer(id);
		} catch (SQLException e1) {
			throw new CouponSystemException("Failed reading customer", e1);
		}
		if (customer == null) {
			throw new CouponSystemException("Customer with id: " + id + " wasn't found on the DB");
		}
		return customer;
	}

	/**
	 * updates the provided customer details excepts the customer name
	 * 
	 * @param customer
	 *            - the customer instance to update
	 * @throws CouponSystemException
	 *             in case customer with id and name doesn't exists on DB, which
	 *             might indicate the user tried to change customer name, which
	 *             is not allowed
	 * @return the number of rows affected in the tables on DB
	 */
	public int updateCustomer(Customer customer) throws CouponSystemException {
		try {
			if (customerDAO.getCustomerByNameAndID(customer.getId(), customer.getCustomerName()) == null) {
				throw new CouponSystemException("customer with id '" + customer.getId() + "' and name '"
						+ customer.getCustomerName() + "' doesn't exists");
			}
			return customerDAO.updateCustomer(customer);
		} catch (SQLException e1) {
			throw new CouponSystemException("Failed updating customer", e1);
		}
	}

	/**
	 * deletes the provided customer from the DB
	 * 
	 * @param customer
	 *            - the customer instance to deleted
	 * @throws CouponSystemException 
	 * @return the number of rows affected from the delete customer operation in the tables on DB
	 */
	public int deleteCustomer(Customer customer) throws CouponSystemException {
		int rowsAffected=0;
		try {
			if (customerDAO.readCustomer(customer.getId()) == null) {
				throw new CouponSystemException("customer with id '" + customer.getId() + "wasn't found");
			}
			rowsAffected+=customerDAO.deleteCustomer(customer);
			rowsAffected+=custCoupDAO.deleteCustomer(customer.getId());
		} catch (SQLException e1) {
			throw new CouponSystemException("Failed deleting customer", e1);
		}
		return rowsAffected;
	}

	/**
	 * returns a collection of all the customers registered on DB
	 * @throws CouponSystemException
	 * @return Collection of type Customer
	 */
	public Collection<Customer> getAllCustomers() throws CouponSystemException {
		Collection<Customer> customers = new ArrayList<Customer>();
		try {
			customers = customerDAO.getAllCustomers();
		} catch (SQLException e1) {
			throw new CouponSystemException("Failed reading customers", e1);
		}
		return customers;
	}

}
