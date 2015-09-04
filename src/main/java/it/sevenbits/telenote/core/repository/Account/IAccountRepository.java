package it.sevenbits.telenote.core.repository.Account;


import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import it.sevenbits.telenote.core.repository.RepositoryException;

public interface IAccountRepository {

    void changeStyle(UserDetailsImpl user) throws RepositoryException;

    void changeUsername(UserDetailsImpl user) throws RepositoryException;

    void changePass(UserDetailsImpl user) throws RepositoryException;

    String getUserStyle(Long userId) throws RepositoryException;
}
