package ru.clevertec.knyazev.service.exception;

public class ServiceException extends RuntimeException {

    public static final String DEFAULT_ERROR_MESSAGE = "Searching object(s) not found";

    public ServiceException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}
