package it.sevenbits.telenote.core.repository;

public class RepositoryException extends Exception {
    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(String s, Exception e) {
        super(s, e);
    }
}
