package com.core.facades;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;

import com.core.beans.Company;
import com.core.beans.Coupon;
import com.core.beans.CouponType;
import com.core.dbdao.CompanyCouponDBDAO;
import com.core.dbdao.CompanyDBDAO;
import com.core.dbdao.CouponDBDAO;
import com.core.dbdao.CustomerCouponDBDAO;
import com.core.exceptions.CouponSystemException;

/**
 * A facade class of an Company client
 */
public class CompanyFacade implements ClientFacade {
	private final long companyID;
	private CouponDBDAO couponDAO;
	private CompanyDBDAO companyDAO;
	private CompanyCouponDBDAO compCoupDAO;
	private CustomerCouponDBDAO custCoupDAO;

	public CompanyFacade(long companyID) throws CouponSystemException {
		this.companyID = companyID;
		try {
			couponDAO = new CouponDBDAO();
			companyDAO = new CompanyDBDAO();
			compCoupDAO = new CompanyCouponDBDAO();
			custCoupDAO = new CustomerCouponDBDAO();
		} catch (Exception e) {
			throw new CouponSystemException("Failed loading system", e);
		}
	}

	public long getCompID() {
		return companyID;
	}

	/**
	 * adds a provided coupon into DB if a coupon with the same name doesn't
	 * already exists on DB
	 * 
	 * @param coupon
	 *            - instance to add into DB
	 * @throws CouponSystemExeption
	 *             in case a coupon with the same name already exists on DB or
	 *             failed operation
	 * @return the id of the created coupon
	 */
	public Long createCoupon(Coupon coupon) throws CouponSystemException {
		Long couponID = null;
		try {
			if (couponDAO.getCouponByTitle(coupon.getTitle()) != null) {
				throw new CouponSystemException(
						"Coupon with the name '" + coupon.getTitle() + "' already exists on DB");
			}
			if (Date.valueOf(coupon.getStartDate()).after(Date.valueOf(coupon.getEndDate()))){
				throw new CouponSystemException("Wrong input: Expiration date input is sonner than starting coupon date input");
			}
			if(coupon.getStartDate().equals(coupon.getEndDate())){
				throw new CouponSystemException("Wrong input: coupon starting date and expiration date are the same");
			}
			Date currentDate= new Date(new GregorianCalendar().getTimeInMillis());
			if (Date.valueOf(coupon.getStartDate()).before(Date.valueOf(currentDate.toLocalDate()))){
				throw new CouponSystemException("Wrong input: Starting coupon date input is sooner than todays current date");
			}
			couponID = couponDAO.createCoupon(coupon);
			compCoupDAO.createCompanyCoupon(companyID, couponID);
		} catch (SQLException e1) {
			throw new CouponSystemException("Failed creating coupon", e1);
		}
		return couponID;
	}

	/**
	 * returns a coupon from DB by the provided id
	 * 
	 * @param id
	 *            - the id of the desired coupon
	 * @throws CouponSystemException
	 *             in case coupon ID doesn't exists on the company's coupons
	 * @return a Coupon instance if the id exists in Company_Coupon table on DB
	 */
	public Coupon readCoupon(long id) throws CouponSystemException {
		Coupon coupon = null;
		try {
			if (!compCoupDAO.isCouponBelongesToCompany(this.companyID, id)) {
				throw new CouponSystemException(
						"coupon with id '" + id + "' doesn't exist with the coupons of current company");
			}
			coupon = couponDAO.readCoupon(id);
		} catch (SQLException e1) {
			throw new CouponSystemException("Failed reading coupon", e1);
		}
		if (coupon == null) {
			throw new CouponSystemException("Coupon with id: " + id + " wasn't found");
		}
		return coupon;
	}

	/**
	 * updates the price and/or the expired date of a coupon
	 * 
	 * @param coupon
	 *            - an instance of the coupon to update
	 * @throws CouponSystemException
	 *             in case id doesn't exists on the company's coupons
	 * @return the number of rows affected in the DB by the operation
	 */
	public int updateCoupon(Coupon coupon) throws CouponSystemException {
		try {
			if (!compCoupDAO.isCouponBelongesToCompany(this.companyID, coupon.getId())) {
				throw new CouponSystemException(
						"coupon with id '" + coupon.getId() + "' doesn't exist with the coupons of current company");
			}
			Coupon currDBCoup = couponDAO.readCoupon(coupon.getId());
			if (currDBCoup == null) {
				throw new CouponSystemException("coupon with id '" + coupon.getId() + "' wasn't found");
			}
			currDBCoup.setEndDate(coupon.getEndDate().toString());
			currDBCoup.setPrice(coupon.getPrice());
			return couponDAO.updateCoupon(currDBCoup);
		} catch (SQLException e1) {
			throw new CouponSystemException("Failed updating coupon", e1);
		}
	}

	/**
	 * deletes a coupon from the system
	 * 
	 * @param coupon
	 *            - an instance of the coupon to delete
	 * @throws CouponSystemException
	 *             in case id doesn't exists on the company's coupons
	 * @return the number of rows affected by the coupon deletion operation in
	 *         DB
	 */
	public int deleteCoupon(Coupon coupon) throws CouponSystemException {
		int rowsAffected = 0;
		try {
			if (!compCoupDAO.isCouponBelongesToCompany(this.companyID, coupon.getId())) {
				throw new CouponSystemException(
						"coupon with id '" + coupon.getId() + "' doesn't exist with the coupons of current company");
			}
			rowsAffected += couponDAO.deleteCoupon(coupon);
			rowsAffected += compCoupDAO.deleteCoupon(coupon.getId());
			rowsAffected += custCoupDAO.deleteCoupon(coupon.getId());
		} catch (SQLException e1) {
			throw new CouponSystemException("Failed deleting coupon", e1);
		}
		return rowsAffected;
	}

	/**
	 * gets all the coupons of the current company
	 * 
	 * @throws CouponSystemException
	 * @return collection of Coupon of the current company
	 */
	public Collection<Coupon> getAllCoupons() throws CouponSystemException {
		Collection<Coupon> compCoups = new ArrayList<Coupon>();
		try {
			Collection<Long> compCoupIDs = compCoupDAO.readCompanyCouponIDs(this.companyID);
			for (Long id : compCoupIDs) {
				compCoups.add(couponDAO.readCoupon(id));
			}
		} catch (SQLException e1) {
			throw new CouponSystemException("Failed reading coupons", e1);
		}
		return compCoups;
	}

	/**
	 * gets coupons of the current company by chosen type
	 * 
	 * @param couponType
	 *            - the type of the searched coupons
	 * @throws CouponSystemException
	 * @return collection of Coupon of the current company of the provided type
	 */
	public Collection<Coupon> getCouponsByType(CouponType couponType) throws CouponSystemException {
		Collection<Coupon> companyCoupons = this.getAllCoupons();
		Collection<Coupon> couponsByType = new ArrayList<Coupon>();
		for (Coupon currCoup : companyCoupons) {
			if (currCoup.getType().equals(couponType.toString())) {
				couponsByType.add(currCoup);
			}
		}
		return couponsByType;
	}

	/**
	 * gets coupons of the current company by top price
	 * 
	 * @param price
	 *            - the top price of the searched coupons
	 * @throws CouponSystemException
	 * @return collection of Coupon of the current company which price are lower
	 *         then the provided top price parameter
	 */
	public Collection<Coupon> getCouponsByTopPrice(double price) throws CouponSystemException {
		Collection<Coupon> companyCoupons = this.getAllCoupons();
		Collection<Coupon> couponsByPrice = new ArrayList<Coupon>();
		for (Coupon currCoup : companyCoupons) {
			if (currCoup.getPrice() <= price) {
				couponsByPrice.add(currCoup);
			}
		}
		return couponsByPrice;
	}

	/**
	 * gets coupons of the current company by expiration date
	 * 
	 * @param endDate
	 *            - the latest end date of the searched coupons
	 * @throws CouponSystemException
	 * @return collection of Coupon of the current company which their end dates
	 *         are earlier then the provided date parameter
	 */
	public Collection<Coupon> getCouponByEndDate(Date endDate) throws CouponSystemException {
		Collection<Coupon> companyCoupons = this.getAllCoupons();
		Collection<Coupon> couponsByDate = new ArrayList<Coupon>();
		for (Coupon currCoup : companyCoupons) {
			if (Date.valueOf(currCoup.getEndDate()).before(endDate)) {
				couponsByDate.add(currCoup);
			}
		}
		return couponsByDate;
	}

	/**
	 * shows the details of the current company
	 * 
	 * @throws CouponSystemException
	 * @return an instance of the current logged in company
	 */
	public Company returnCurrentCompany() throws CouponSystemException {
		try {
			return companyDAO.readCompany(this.companyID);
		} catch (SQLException e) {
			throw new CouponSystemException("Failed reading company details", e);
		}
	}

}
