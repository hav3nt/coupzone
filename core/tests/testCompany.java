package com.core.tests;

import java.sql.Date;
import java.util.GregorianCalendar;

import com.core.beans.Company;
import com.core.beans.Coupon;
import com.core.beans.CouponType;
import com.core.exceptions.CouponSystemException;
import com.core.facades.CompanyFacade;
import com.core.facades.clientType;
import com.core.system.CouponSystem;

/**
 * A class demonstrating the company client possibilities
 * 
 * @author Baruch
 * */
public class testCompany {
	public static void main(String[] args) {

		CouponSystem system = null;
		try {
			system = CouponSystem.getInstance();
			CompanyFacade comp = (CompanyFacade) system.login("game", "new123",
					clientType.COMPANY);
			Date start=Date.valueOf("2016-01-25");
			Date end=Date.valueOf("2016-01-24");
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
					new Date(5000), 26, CouponType.CAMPING, "kjh", 139,
					"asdas");
			coup3.setId(126);
			Coupon coup4 = new Coupon("nat3", start, end, 1,
					CouponType.FOOD, "all you can eat", 34, "image here");
			coup4.setId(131);
			Coupon coup5 = new Coupon("title", startDate, endDate, 1,
					CouponType.HEALTH, "mesage", 97, "image");
			coup5.setId(129);
			Coupon coup6 = new Coupon("checkNew", new Date(32), new Date(
					new GregorianCalendar().getTimeInMillis() + 3992000000l),
					65, CouponType.SPORTS, "dsfds", 49, "hfgf");
			coup6.setId(6);
			

			// //////////////////////////// ADD COUPON
//			 comp.createCoupon(coup);
//			 comp.createCoupon(coup2);
//			 comp.createCoupon(coup3);
			 comp.createCoupon(coup4);
//			 comp.createCoupon(coup5);
//			 comp.createCoupon(coup6);

			// //////////////////////////// DELETE COUPON
//			 comp.deleteCoupon(coup);
//			 comp.deleteCoupon(coup2);
//			 comp.deleteCoupon(coup3);
//			 comp.deleteCoupon(coup4);
//			 comp.deleteCoupon(coup5);
//			 comp.deleteCoupon(coup6);

			// //////////////////////////// SHOW COUPONS
			System.out.println(comp.getAllCoupons());

			// //////////////////////////// UPDATE COUPON (ONLY END DATE AND
			// PRICE)
			Date newDate = new Date(
					new GregorianCalendar().getTimeInMillis() + 30000000000l);
			coup5.setTitle("new title");
			coup5.setStartDate(new Date(100000).toString());
			coup5.setEndDate(newDate.toString());
			coup5.setAmount(34);
			coup5.setMessage("new message");
			coup5.setPrice(74);
			coup5.setType(CouponType.SPORTS.toString());
			coup5.setId(5);
//			 comp.updateCoupon(coup5);

			// /////////////////////////// SHOW COUPONS BY TYPE
			System.out.println(comp.getCouponsByType(CouponType.FOOD));

			// //////////////////////////// SHOW COUPONS BY TOP PRICE
			System.out.println(comp.getCouponsByTopPrice(134));

			// //////////////////////////// SHOW COUPONS BY END DATE
			System.out.println(comp.getCouponByEndDate(new Date(100000)));

			// //////////////////////////// SHOW CURRENT COMPANY
			System.out.println(comp.returnCurrentCompany());

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// ///////////////////// SHUT DOWN THE SYSTEM
			try {
				if (system != null) {
					system.shutdown();
				}
			} catch (CouponSystemException e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
