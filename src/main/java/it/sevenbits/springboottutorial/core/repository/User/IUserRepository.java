package it.sevenbits.springboottutorial.core.repository.User;

import it.sevenbits.springboottutorial.core.domain.Subscription;
import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.repository.RepositoryException;

/**
 * Created by Admin on 09.07.2015.
 */
public interface IUserRepository {
    public void save(final UserDetailsImpl userDetails) throws RepositoryException;

    public boolean isEmailExists(final UserDetailsImpl userDetails) throws RepositoryException;
}
