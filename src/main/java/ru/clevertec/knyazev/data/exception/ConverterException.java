package ru.clevertec.knyazev.data.exception;

public class ConverterException extends Exception {
	private static final long serialVersionUID = 8001052544037516716L;
	
	public ConverterException(String message) {
		super(message);
	}
	
	public ConverterException(String message, NumberFormatException e) {
		super(message, e);
	}
}
