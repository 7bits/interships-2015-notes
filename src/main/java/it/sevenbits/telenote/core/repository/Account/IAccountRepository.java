package it.sevenbits.telenote.core.repository.Account;


import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import it.sevenbits.telenote.core.repository.RepositoryException;

public interface IAccountRepository {

    void changeStyle(Long userId) throws RepositoryException;

    void changeUsername(Long userId) throws RepositoryException;

    void changePass(Long userId) throws RepositoryException;

    String getUserStyle(Long userId) throws RepositoryException;
}
