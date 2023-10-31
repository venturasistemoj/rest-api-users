package com.venturasistemoj.restapi.exceptions;

/**
 * Exception that handles unsupported operations for <code>Address</code> and <code>PhoneNumber</code> entities.
 *
 * @author Wilson Ventura
 */
public class IllegalOperationException extends RuntimeException {

	private static final long serialVersionUID = -2857553545596762168L;

	public IllegalOperationException(String message) {
		super(message);
	}

}
