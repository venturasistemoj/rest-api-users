package com.venturasistemoj.restapi.domain.address;

/**
 * Exception that handles the consistency of a address's data.
 * @author Wilson Ventura
 */
public class IllegalAddressStateException extends RuntimeException {

	private static final long serialVersionUID = -798284966665385103L;

	public IllegalAddressStateException(String message) {
		super(message);
	}
}
