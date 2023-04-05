package ru.clevertec.knyazev.data.exception;

public class ValidatorException extends Exception {
	private static final long serialVersionUID = -7393583822447702057L;

	public ValidatorException(String message) {
		super(message);
	}
	
	public ValidatorException(String message, Exception e) {
		super(message, e);
	}
}
