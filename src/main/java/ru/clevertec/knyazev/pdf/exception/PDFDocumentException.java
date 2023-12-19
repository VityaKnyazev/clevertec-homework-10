package ru.clevertec.knyazev.pdf.exception;

public class PDFDocumentException extends RuntimeException {
    public PDFDocumentException() {
        super();
    }

    public PDFDocumentException(String message) {
        super(message);
    }

    public PDFDocumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public PDFDocumentException(Throwable cause) {
        super(cause);
    }
}
