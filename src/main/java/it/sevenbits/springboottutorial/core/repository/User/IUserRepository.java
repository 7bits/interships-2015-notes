package it.sevenbits.springboottutorial.core.repository.User;

import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.repository.RepositoryException;
import java.util.Optional;

/**
 * Created by Admin on 09.07.2015.
 */
public interface IUserRepository {
    public void create(final UserDetailsImpl userDetails) throws RepositoryException;

    public boolean isEmailExists(final UserDetailsImpl userDetails) throws RepositoryException;

    public Long getIdByEmail(final UserDetailsImpl userDetails) throws RepositoryException;

    public String getPasswordById(final UserDetailsImpl userDetails) throws RepositoryException;

    public void updatePassword(final UserDetailsImpl userDetails) throws RepositoryException;

    public Optional<UserDetailsImpl> getUserById(Long id)  throws RepositoryException;

    public Optional<UserDetailsImpl> getUserByEmail(String email)  throws RepositoryException;

    public void remove(final UserDetailsImpl user) throws RepositoryException;

    public void confirm(String email) throws RepositoryException;

    public void setTokenByEmail(String email, String token) throws RepositoryException;

    public String getTokenByEmail(String email) throws RepositoryException;
}
