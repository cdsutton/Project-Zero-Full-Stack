package com.revature.exceptions;

public class RealtorNotFoundException extends Exception {

	public RealtorNotFoundException() {
	}

	public RealtorNotFoundException(String message) {
		super(message);
	}

	public RealtorNotFoundException(Throwable cause) {
		super(cause);
	}

	public RealtorNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public RealtorNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
