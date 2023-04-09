package ru.clevertec.knyazev.service.exception;

public class ServiceTransactionException extends RuntimeException {
	private static final long serialVersionUID = -4712932081466592514L;

	public ServiceTransactionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceTransactionException(String message) {
		super(message);
	}
	
}
