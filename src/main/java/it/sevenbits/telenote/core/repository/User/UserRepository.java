package it.sevenbits.telenote.core.repository.User;

import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import it.sevenbits.telenote.core.mappers.UserMapper;
import it.sevenbits.telenote.core.repository.RepositoryException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Qualifier(value = "theUserPersistRepository")
public class UserRepository implements IUserRepository {
    private static Logger LOG = Logger.getLogger(UserRepository.class);

    @Autowired
    private UserMapper mapper;

    /**
     * Adds new user with specified email. username, password, role, enabled flag.
     * @param userDetails POJO for user.
     * @throws RepositoryException
     */
    @Override
    public void create(final UserDetailsImpl userDetails) throws RepositoryException {
        try {
            mapper.create(userDetails);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while saving user: " + e.getMessage(), e);
        }
    }

    /**
     * Checks does email exist by counting records in users with specified email.
     * @param userDetails POJO for user.
     * @return count of records with specified email.
     * @throws RepositoryException
     */
    @Override
    public boolean isEmailExists(final UserDetailsImpl userDetails) throws RepositoryException {
        try {
            return mapper.isEmailExists(userDetails) == 0 ? false : true;
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while checking email existence: " + e.getMessage(), e);
        }
    }

    /**
     * Gets user id by user email.
     * @param userDetails POJO for user.
     * @return user id.
     * @throws RepositoryException
     */
    @Override
    public Long getIdByEmail(final UserDetailsImpl userDetails) throws RepositoryException {
        try {
            return mapper.getIdByEmail(userDetails) == null ? -1 : (Long) mapper.getIdByEmail(userDetails);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting user_id by email: " + e.getMessage(), e);
        }
    }

    /**
     * Gets user password(hash) by user id.
     * @param userDetails POJO for user.
     * @return user password(hash).
     * @throws RepositoryException
     */
    @Override
    public String getPasswordById(final UserDetailsImpl userDetails) throws RepositoryException {
        try {
            return mapper.getPasswordById(userDetails);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting password by user_id: " + e.getMessage(), e);
        }
    }

    /**
     * Updates user password(hash) by user email.
     * @param userDetails POJO for user.
     * @throws RepositoryException
     */
    @Override
    public void updatePassword(final UserDetailsImpl userDetails) throws RepositoryException {
        try {
            mapper.updatePassword(userDetails);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while updating password: " + e.getMessage(), e);
        }
    }

    /**
     * Gets user data by user id.
     * @param userId user id.
     * @return user data by user id.
     * @throws RepositoryException
     */
    @Override
    public Optional<UserDetailsImpl> getUserById(Long userId) throws RepositoryException {
        try {
            return Optional.ofNullable(mapper.getUserById(userId));
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting user by id: " + e.getMessage(), e);
        }
    }

    /**
     * Gets user data by user email.
     * @param email user email.
     * @return user data by user email.
     * @throws RepositoryException
     */
    @Override
    public Optional<UserDetailsImpl> getUserByEmail(String email) throws RepositoryException {
        try {
            return Optional.ofNullable(mapper.getUserByEmail(email));
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting user by email: " + e.getMessage(), e);
        }
    }

    /**
     * Removes user from users by email or id.
     * @param user POJO for user.
     * @throws RepositoryException
     */
    @Override
    @Description("You can remove users by email or by id.")
    public void remove(final UserDetailsImpl user) throws RepositoryException {
        try {
            mapper.remove(user);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while removing user: " + e.getMessage(), e);
        }
    }

    /**
     * Sets user is_confirmed flag to true by email.
     * @param email user email.
     * @throws RepositoryException
     */
    @Override
    public void confirm(String email) throws RepositoryException {
        try {
            mapper.confirm(email);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while confirming email: " + e.getMessage(), e);
        }
    }

    /**
     * Sets user token by user email
     * @param email user email.
     * @param token user token.
     * @throws RepositoryException
     */
    @Override
    public void setTokenByEmail(String email, String token) throws RepositoryException {
        try {
            mapper.setTokenByEmail(email, token);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while setting token to user by email: " + e.getMessage(), e);
        }
    }

    /**
     * Gets user token by user email.
     * @param email user email.
     * @return user token by user email.
     * @throws RepositoryException
     */
    @Override
    public String getTokenByEmail(String email) throws RepositoryException {
        try {
            return mapper.getTokenByEmail(email);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting token by email: " + e.getMessage(), e);
        }
    }

    /**
     * Clean all records from users, notes, usernotes.
     * @throws RepositoryException
     */
    @Override
    public void cleanDB() throws RepositoryException {
        mapper.deleteUsers();
        mapper.deleteNotes();
        mapper.deleteUsernotes();
    }
}
