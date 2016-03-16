package com.adanac.framework.log.exception;

/**
 * 
 * @author adanac
 * @version 1.0
 */
public class LogException extends RuntimeException {

	private static final long serialVersionUID = -911729004899556960L;

	public LogException() {
		super();
	}

	public LogException(String message) {
		super(message);
	}

	public LogException(String message, Throwable cause) {
		super(message, cause);
	}

	public LogException(Throwable cause) {
		super(cause);
	}
}