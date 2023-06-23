package com.venturasistemoj.restapi.domain.phone;

/**
 * Exception that handles the consistency of a phone's data.
 * @author Wilson Ventura
 */
public class IllegalPhoneStateException extends RuntimeException {

	private static final long serialVersionUID = 4933108739284094428L;

	public IllegalPhoneStateException(String message) {
		super(message);
	}
}
