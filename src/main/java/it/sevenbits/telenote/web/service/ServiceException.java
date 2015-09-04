package it.sevenbits.telenote.web.service;

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
