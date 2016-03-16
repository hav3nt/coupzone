package com.core.system;

import java.sql.SQLException;

import com.core.dbdao.CompanyCouponDBDAO;
import com.core.dbdao.CompanyDBDAO;
import com.core.dbdao.CouponDBDAO;
import com.core.dbdao.CustomerCouponDBDAO;
import com.core.dbdao.CustomerDBDAO;
import com.core.dblayer.ConnectionPool;
import com.core.exceptions.CouponSystemException;
import com.core.facades.AdminFacade;
import com.core.facades.ClientFacade;
import com.core.facades.CompanyFacade;
import com.core.facades.CustomerFacade;
import com.core.facades.clientType;
import com.core.threads.DailyCouponExpirationTask;


/**
 * A class representing the coupon system, allows the login and shut down
 * 
 * @author Baruch
 */
public class CouponSystem {

	private CompanyDBDAO companyDAO;
	private CustomerDBDAO customerDAO;
	private CouponDBDAO couponDAO;
	private CompanyCouponDBDAO companyCouponDAO;
	private CustomerCouponDBDAO customerCouponDAO;
//	private DailyCouponExpirationTask expirationTask;
//	private Thread couponExpireThread;
	private ConnectionPool connectionPool;
	private static CouponSystem instance;

	static {
		try {
			instance = new CouponSystem();
		} catch (ExceptionInInitializerError | Exception e) {
			throw new RuntimeException("System loading failure", e.getCause());
		}
	}

	public static CouponSystem getInstance() throws Exception {
		return instance;
	}

	/**
	 * A constructor which loads DAOs, connection pool to the DB, and starts
	 * daily coupons thread
	 */
	private CouponSystem() throws Exception {
		connectionPool = ConnectionPool.getInstance();
		companyDAO = new CompanyDBDAO();
		customerDAO = new CustomerDBDAO();
		couponDAO = new CouponDBDAO();
		companyCouponDAO = new CompanyCouponDBDAO();
		customerCouponDAO = new CustomerCouponDBDAO();
//		expirationTask = new DailyCouponExpirationTask();
//		couponExpireThread = new Thread(expirationTask);
//		couponExpireThread.start();
	}

	/**
	 * A login method to the coupon system
	 * 
	 * @param name
	 *            - the username
	 * @param password
	 *            - the password
	 * @param type
	 *            - the type of the client
	 * @return ClientFacade of type like the client type parameter
	 * @throws CouponSystemException
	 */
	public ClientFacade login(String name, String password, clientType type)
			throws CouponSystemException {
		try {
			switch (type) {
			case CUSTOMER:
				Long CustomerID = customerDAO.login(name, password);
				if (CustomerID == null) {
					throw new CouponSystemException(
							"Login failed, wrong customer name or password");
				}
				return new CustomerFacade(CustomerID);
			case COMPANY:
				Long CompanyID = companyDAO.login(name, password);
				if (CompanyID == null) {
					throw new CouponSystemException(
							"Login failed, wrong company name or password");
				}
				return new CompanyFacade(CompanyID);
			case ADMIN:
				if (!(name.equals("admin") && password.equals("1234"))) {
					throw new CouponSystemException(
							"Login failed, wrong admin name or password");
				}
				return new AdminFacade();
			default:
				break;
			}
		} catch (SQLException e) {
			throw new CouponSystemException("Login operation failed", e);
		}
		return null;
	}

	/**
	 * shuts down the coupon system
	 * 
	 * @throws CouponSystemException
	 */
	public void shutdown() throws CouponSystemException {
		try {
//			this.expirationTask.stopTask();
//			this.couponExpireThread.interrupt();
			this.connectionPool.closeAllConnections();
		} catch (SQLException e) {
			throw new CouponSystemException("Failed system shutdown", e);
		}
	}

}
