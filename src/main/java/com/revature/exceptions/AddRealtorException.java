package com.revature.exceptions;

public class AddRealtorException extends Exception {

	public AddRealtorException() {
	}

	public AddRealtorException(String message) {
		super(message);
	}

	public AddRealtorException(Throwable cause) {
		super(cause);
	}

	public AddRealtorException(String message, Throwable cause) {
		super(message, cause);
	}

	public AddRealtorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
