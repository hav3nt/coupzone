package com.core.tests;

import java.sql.Date;
import java.util.GregorianCalendar;

import com.core.beans.Coupon;
import com.core.beans.CouponType;
import com.core.exceptions.CouponSystemException;
import com.core.facades.CustomerFacade;
import com.core.facades.clientType;
import com.core.system.CouponSystem;

/**
 * A class demonstrating the customer client possibilities
 * 
 * @author Baruch
 * */
public class testCustomer {
	public static void main(String[] args) {
		try {
			CouponSystem system = CouponSystem.getInstance();
			CustomerFacade cust = (CustomerFacade) system.login("cust2",
					"12345", clientType.CUSTOMER);

			Date startDate = new Date(new GregorianCalendar().getTimeInMillis());
			Date endDate = new Date(
					new GregorianCalendar().getTimeInMillis() + 2592000000l);
			Coupon coup = new Coupon("razor", new Date(123), endDate, 1,
					CouponType.ELECTRICITY, "new razor", 234, "image1");
			coup.setId(122);
			Coupon coup2 = new Coupon("tent", new Date(200000), endDate, 26,
					CouponType.CAMPING, "2mm tent", 76, "image2");
			coup2.setId(123);
			Coupon coup3 = new Coupon("khj4", new Date(123),
					new Date(500000000), 26, CouponType.CAMPING, "kjh", 139,
					"asdas");
			coup3.setId(3);
			Coupon coup4 = new Coupon("nat2", startDate, endDate, 1,
					CouponType.FOOD, "all you can eat", 34, "image here");
			coup4.setId(4);
			Coupon coup5 = new Coupon("title", startDate, endDate, 1,
					CouponType.HEALTH, "mesage", 97, "image");
			coup5.setId(126);
			Coupon coup6 = new Coupon("checkNew", new Date(32), new Date(
					new GregorianCalendar().getTimeInMillis() + 3992000000l),
					65, CouponType.SPORTS, "dsfds", 49, "hfgf");
			coup6.setId(127);

			// /////////////////// PURCHASE COUPON
//			cust.purchaseCoupon(coup);
//			cust.purchaseCoupon(coup2);
//			cust.purchaseCoupon(coup3);
//			cust.purchaseCoupon(coup4);
//			cust.purchaseCoupon(coup5);
//			cust.purchaseCoupon(coup6);

			// /////////////////// VIEW PURCHASED COUPONS
			System.out.println(cust.getPurchasedCoupons());

			// /////////////////// VIEW PURCHASED COUPONS BY PRICE
			System.out.println(cust.getPurchasedCouponsByPrice(75));

			// /////////////////// VIEW PURCHASED COUPONS BY TYPE
			System.out.println(cust
					.getPurchasedCouponsByType(CouponType.CAMPING));
			
			// ///////////////////// SHUT DOWN THE SYSTEM  
			 system.shutdown();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
