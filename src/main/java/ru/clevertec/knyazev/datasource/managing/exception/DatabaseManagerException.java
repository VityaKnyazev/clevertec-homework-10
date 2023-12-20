package ru.clevertec.knyazev.datasource.managing.exception;

public class DatabaseManagerException extends RuntimeException {
    public DatabaseManagerException() {
    }

    public DatabaseManagerException(String message) {
        super(message);
    }

    public DatabaseManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseManagerException(Throwable cause) {
        super(cause);
    }
}
