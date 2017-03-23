package com.core.tests;

import com.core.beans.Company;
import com.core.beans.Customer;
import com.core.exceptions.CouponSystemException;
import com.core.facades.AdminFacade;
import com.core.facades.clientType;
import com.core.system.CouponSystem;

/**
 * A class demonstrating the admin client possibilities
 * 
 * @author Baruch
 * */
public class testAdmin {
	public static void main(String[] args) {
		CouponSystem system = null;
		try {
			system = CouponSystem.getInstance();

			AdminFacade user = (AdminFacade) system.login("admin", "1234",
					clientType.ADMIN);

			// ///////////////////// ADD COMPANY
			System.out.println("adding companies:");
			Company c = new Company("Gags", "lol", "lo@lgmail.com");
//			 c.setId(user.createCompany(c));
			Company c2 = new Company("games", "new123", "game@game.play");
//			 c2.setId(user.createCompany(c2));
			 System.out.println();

			// ///////////////////// ADD CUSTOMER
			 System.out.println("adding customers:");
			Customer cu = new Customer("cust1", "encrypted");
//			cu.setId(user.createCustomer(cu));
			Customer cu2 = new Customer("cust4", "12345");
//			 cu2.setId(user.createCustomer(cu2));
			 System.out.println();

			// ///////////////////// GET ALL COMPANIES
			System.out.println(user.getAllCompanies());
			System.out.println();

			// ///////////////////// UPDATE COMPANY
			System.out.println("updating company Gags:");
//			c.setId(108);
			c.setCompanyName("Gags");
			c.setPassword("newpasslol");
			c.setEmail("changed mail");
//			 user.updateCompany(c);
			 System.out.println();

			// ///////////////////// READ COMPANY
			 System.out.println("reading company after update");
//			 System.out.println(user.readCompany(c.getId()));
			 System.out.println();

			// ///////////////////// GET ALL CUSTOMERS
			System.out.println(user.getAllCustomers());
			System.out.println();

			// ///////////////////// UPDATE CUSTOMER
			System.out.println("updating customer cust1");
//			cu.setId(1);
			cu.setCustomerName("cust1");
			cu.setPassword("new encrypted2");
//			 user.updateCustomer(cu);
			 System.out.println();
			 
			// ///////////////////// READ CUSTOMER
			 System.out.println("reading company after update");
			 System.out.println(user.readCustomer(cu.getId()));
			 System.out.println();

			// ///////////////////// DELETE CUSTOMER
			 System.out.println("deleting customer cust4");
//			cu2.setId(2);
//			 user.deleteCustomer(cu2);
			 System.out.println("after deleting all customers are");
			 System.out.println(user.getAllCustomers());

			// ///////////////////// DELETE COMPANY
			 System.out.println("deleting company Gags");
//			c.setId(1);
//			 user.deleteCompany(c);
			 System.out.println("after deleting all companies are");
			 System.out.println(user.getAllCompanies());
			 
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
