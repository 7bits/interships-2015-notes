package it.sevenbits.telenote.service;

/**
 * Exception for services.
 */
public class ServiceException extends Exception {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException() {

    }

    public ServiceException(String s, Exception e) {
        super(s, e);

    }
}
