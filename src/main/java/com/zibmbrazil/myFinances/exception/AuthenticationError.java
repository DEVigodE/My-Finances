package com.zibmbrazil.myFinances.exception;

public class AuthenticationError extends RuntimeException {

	public AuthenticationError(String msg) {
		super(msg);
	}
}