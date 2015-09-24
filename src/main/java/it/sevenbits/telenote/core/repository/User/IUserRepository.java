package it.sevenbits.telenote.core.repository.User;

import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import it.sevenbits.telenote.core.repository.RepositoryException;
import java.util.Optional;

/**
 * Interface for UserRepository.
 */
public interface IUserRepository {
    public void create(final UserDetailsImpl userDetails) throws RepositoryException;

    public boolean isEmailExists(final UserDetailsImpl userDetails) throws RepositoryException;

    public Long getIdByEmail(final UserDetailsImpl userDetails) throws RepositoryException;

    public String getPasswordById(final UserDetailsImpl userDetails) throws RepositoryException;

    public void updatePassword(final UserDetailsImpl userDetails) throws RepositoryException;

    public Optional<UserDetailsImpl> getUserById(Long userId)  throws RepositoryException;

    public Optional<UserDetailsImpl> getUserByEmail(String email)  throws RepositoryException;

    public void remove(final UserDetailsImpl user) throws RepositoryException;

    public void confirm(String email) throws RepositoryException;

    public void setTokenByEmail(String email, String token) throws RepositoryException;

    public String getTokenByEmail(String email) throws RepositoryException;

    public void cleanDB() throws  RepositoryException;
}
