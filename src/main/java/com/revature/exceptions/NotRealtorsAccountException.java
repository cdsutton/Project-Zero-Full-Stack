package com.revature.exceptions;

public class NotRealtorsAccountException extends Exception {

	public NotRealtorsAccountException() {
	}

	public NotRealtorsAccountException(String message) {
		super(message);
	}

	public NotRealtorsAccountException(Throwable cause) {
		super(cause);
	}

	public NotRealtorsAccountException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotRealtorsAccountException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
