package com.core.facades;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;

import com.core.beans.Coupon;
import com.core.beans.CouponType;
import com.core.beans.Customer;
import com.core.dbdao.CouponDBDAO;
import com.core.dbdao.CustomerCouponDBDAO;
import com.core.dbdao.CustomerDBDAO;
import com.core.exceptions.CouponSystemException;

/**
 * A facade class of an Customer client
 */
public class CustomerFacade implements ClientFacade {

	private final long custID;
	private CouponDBDAO couponDAO;
	private CustomerDBDAO customerDAO;
	private CustomerCouponDBDAO custCoupDAO;

	public CustomerFacade(long custID) throws CouponSystemException {
		this.custID = custID;
		try {
			couponDAO = new CouponDBDAO();
			customerDAO = new CustomerDBDAO();
			custCoupDAO = new CustomerCouponDBDAO();
		} catch (Exception e) {
			throw new CouponSystemException("Failed loading system", e);
		}
	}

	public long getCustomerID() {
		return custID;
	}

	/**
	 * purchases a coupon from the system
	 * 
	 * @param oupon
	 *            - the coupon to be purchase
	 * @throws CouponSystemException
	 *             if coupon was boughten before by the customer or, if coupon
	 *             was expired or, if the amount of this coupon is 0
	 * @return the number of rows affected from the purchase operation
	 */
	public int purchaseCoupon(Coupon coupon) throws CouponSystemException {
		try {
			Coupon currDBCoup = couponDAO.readCoupon(coupon.getId());
			if (currDBCoup == null) {
				throw new CouponSystemException("Failed finding coupon on system");
			}
			if (custCoupDAO.isCouponPurchasedByCustomer(this.custID,
					coupon.getId())) {
				throw new CouponSystemException(
						"can't purchase a coupon more than once");
			}
			Date currentDate= new Date(new GregorianCalendar().getTimeInMillis());
			if (Date.valueOf(currDBCoup.getEndDate()).before(
					Date.valueOf(currentDate.toLocalDate()))) {
				throw new CouponSystemException("coupon was expired");
			}
			if (Date.valueOf(currDBCoup.getStartDate()).after(
					Date.valueOf(currentDate.toLocalDate()))) {
				throw new CouponSystemException("coupon deal time has not been started yet");
			}
			if (currDBCoup.getAmount() < 1) {
				throw new CouponSystemException("coupon is sold out");
			}
			custCoupDAO.createCustomerCoupon(this.custID, currDBCoup.getId());
			currDBCoup.setAmount(currDBCoup.getAmount() - 1);
			return couponDAO.updateCoupon(currDBCoup);
		} catch (SQLException e1) {
			throw new CouponSystemException("Failed purchasing coupon", e1);
		}
	}

	/**
	 * returns all of the current customer's coupons
	 * 
	 * @throws CouponSystemException
	 * @return a collection of coupons of the current customer
	 */
	public Collection<Coupon> getPurchasedCoupons()
			throws CouponSystemException {
		Collection<Coupon> customerCoupons = new ArrayList<Coupon>();
		try {
			Collection<Long> customerCouponsIDs = custCoupDAO
					.readCustomerCouponIDs(this.custID);
			for (Long id : customerCouponsIDs) {
				customerCoupons.add(couponDAO.readCoupon(id));
			}
		} catch (SQLException e1) {
			throw new CouponSystemException("Failed reading coupons", e1);
		}
		return customerCoupons;
	}

	/**
	 * returns all of the current customer's coupons from a specific type
	 * 
	 * @param type
	 *            - the searched coupon's type
	 * @throws CouponSystemException
	 * @return a collection of coupons of the current customer of the provided
	 *         type
	 */
	public Collection<Coupon> getPurchasedCouponsByType(CouponType type)
			throws CouponSystemException {
		Collection<Coupon> customerCoupons = this.getPurchasedCoupons();
		Collection<Coupon> couponsByType = new ArrayList<Coupon>();
		for (Coupon currCoup : customerCoupons) {
			if (currCoup.getType().equals(type.toString())) {
				couponsByType.add(currCoup);
			}
		}
		return couponsByType;
	}

	/**
	 * returns all of the current customer's coupons
	 * 
	 * @param price
	 *            - the top searched price
	 * @throws CouponSystemException
	 * @return a collection of coupons of the current customer with a lower
	 *         price than provided parameter
	 */
	public Collection<Coupon> getPurchasedCouponsByPrice(double price)
			throws CouponSystemException {
		Collection<Coupon> customerCoupons = this.getPurchasedCoupons();
		Collection<Coupon> couponsByPrice = new ArrayList<Coupon>();
		for (Coupon currCoup : customerCoupons) {
			if (currCoup.getPrice() <= price) {
				couponsByPrice.add(currCoup);
			}
		}
		return couponsByPrice;
	}

	/**
	 * returns all of the system coupons
	 * 
	 * @throws CouponSystemException
	 * @return a collection of coupons of the system
	 */
	public Collection<Coupon> getSystemCoupons() throws CouponSystemException {
		try {
			return couponDAO.getAllCoupons();
		} catch (SQLException e) {
			throw new CouponSystemException("Failed reading coupon list", e);
		}
	}

	/**
	 * shows the details of the current customer
	 * 
	 * @throws CouponSystemException
	 * @return Customer - the current logged in customer
	 */
	public Customer returnCurrentCustomer() throws CouponSystemException {
		try {
			return customerDAO.readCustomer(this.custID);
		} catch (SQLException e) {
			throw new CouponSystemException("Failed reading customer details",
					e);
		}
	}

}
