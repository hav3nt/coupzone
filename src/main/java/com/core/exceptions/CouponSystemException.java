package com.core.exceptions;

/**
 * A class for the coupon system exceptions
 * 
 * @author Baruch
 * */
public class CouponSystemException extends Exception {

	private static final long serialVersionUID = 1L;

	public CouponSystemException(String message) {
		super(message);
	}

	public CouponSystemException(String message, Exception e) {
		super(message, e);
	}

	public CouponSystemException(String message, ExceptionInInitializerError ex) {
		super(message, ex);
	}

	public Throwable getCause() {
		return super.getCause();
	}

}
