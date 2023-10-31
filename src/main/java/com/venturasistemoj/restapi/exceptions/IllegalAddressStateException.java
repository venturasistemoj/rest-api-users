package com.venturasistemoj.restapi.exceptions;

/**
 * Exception that handles data consistency of an address.
 *
 * @author Wilson Ventura
 */
public class IllegalAddressStateException extends RuntimeException {

	private static final long serialVersionUID = -798284966665385103L;

	public IllegalAddressStateException(String message) {
		super(message);
	}
}
