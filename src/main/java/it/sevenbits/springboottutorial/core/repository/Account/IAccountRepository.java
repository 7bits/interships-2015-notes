package it.sevenbits.springboottutorial.core.repository.Account;


import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.repository.RepositoryException;

public interface IAccountRepository {

    void changeTheme(UserDetailsImpl user) throws RepositoryException;

    void changeUsername(UserDetailsImpl user) throws RepositoryException;

    void changePass(UserDetailsImpl user) throws RepositoryException;
}
