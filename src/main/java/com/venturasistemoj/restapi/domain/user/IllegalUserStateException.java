package com.venturasistemoj.restapi.domain.user;

/**
 * Exception that handles the consistency of a user's data.
 * @author Wilson Ventura
 */
public class IllegalUserStateException extends RuntimeException {

	private static final long serialVersionUID = -4132961364431664259L;

	public IllegalUserStateException(String message) {
		super(message);
	}
}
