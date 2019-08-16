package com.dataseek.xe.config;

public class JdbcException extends RuntimeException {
	private static final long serialVersionUID = 5275547407909132424L;

	public JdbcException() {
		super();
	}

	public JdbcException(String message, Throwable cause) {
		super(message, cause);
	}

	public JdbcException(String message) {
		super(message);
	}

	public JdbcException(Throwable cause) {
		super(cause);
	}
}
