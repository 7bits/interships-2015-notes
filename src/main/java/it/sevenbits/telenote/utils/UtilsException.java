package it.sevenbits.telenote.utils;


/**
 * Exception for services.
 */
public class UtilsException extends Exception {
    public UtilsException(String message) {
        super(message);
    }

    public UtilsException() {

    }

    public UtilsException(String s, Exception e) {
        super(s, e);

    }
}
