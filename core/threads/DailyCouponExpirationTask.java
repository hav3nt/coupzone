package com.core.threads;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Collection;
import java.util.GregorianCalendar;

import com.core.beans.Coupon;
import com.core.dbdao.CompanyCouponDBDAO;
import com.core.dbdao.CouponDBDAO;
import com.core.dbdao.CustomerCouponDBDAO;
import com.core.exceptions.CouponSystemException;

/**
 * deletes every 24 hours, the expired coupons on the system
 * 
 * @author Baruch
 * */
public class DailyCouponExpirationTask implements Runnable {

	private CouponDBDAO couponDAO;
	private CompanyCouponDBDAO companyCouponDAO;
	private CustomerCouponDBDAO customerCouponDAO;
	private boolean quite;

	public DailyCouponExpirationTask() throws CouponSystemException {
		this.quite = false;
		try {
			couponDAO = new CouponDBDAO();
			companyCouponDAO = new CompanyCouponDBDAO();
			customerCouponDAO = new CustomerCouponDBDAO();
		} catch (Exception e) {
			throw new CouponSystemException("System operation failure", e);
		}
	}

	/**
	 * runs each 24 hours over the system coupons and deletes the coupons which
	 * were expired
	 * */
	@Override
	public void run() {
		while (!quite) {
			Collection<Coupon> coupons;
			try {
				coupons = couponDAO.getAllCoupons();
				Date currDate = new Date(
						new GregorianCalendar().getTimeInMillis());
				for (Coupon currCoup : coupons) {
					if (Date.valueOf(currCoup.getEndDate()).before(currDate)) {
						couponDAO.deleteCoupon(currCoup);
						companyCouponDAO.deleteCoupon(currCoup.getId());
						customerCouponDAO.deleteCoupon(currCoup.getId());
					}
				}
			} catch (SQLException e) {
				System.out.println("System operation failure: "+e.getMessage());
			}

			try {
				Thread.sleep(1000 * 60 * 60 * 24);
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * stops the thread
	 * */
	public void stopTask() {
		quite = true;
	}

}
