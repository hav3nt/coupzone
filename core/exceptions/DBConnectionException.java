package com.core.exceptions;

/**
 * A class for the coupon system exceptions
 * 
 * @author Baruch
 * */
public class DBConnectionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DBConnectionException(String message) {
		super(message);
	}

	public DBConnectionException(String message, Throwable e) {
		super(message, e);
	}

	public DBConnectionException(String message, ExceptionInInitializerError ex) {
		super(message, ex);
	}

	public Throwable getCause() {
		return super.getCause();
	}

}

