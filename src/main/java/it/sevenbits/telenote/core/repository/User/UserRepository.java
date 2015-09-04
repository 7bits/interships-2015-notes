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

    @Override
    public void create(final UserDetailsImpl userDetails) throws RepositoryException {
        try {
            mapper.insert(userDetails);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while saving user: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isEmailExists(final UserDetailsImpl userDetails) throws RepositoryException {
        try {
            return mapper.isEmailExists(userDetails) == 0 ? false : true;
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while checking email existence: " + e.getMessage(), e);
        }
    }

    @Override
    public Long getIdByEmail(final UserDetailsImpl userDetails) throws RepositoryException {
        try {
            return mapper.getIdByEmail(userDetails) == null ? -1 : (Long) mapper.getIdByEmail(userDetails);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting user_id by email: " + e.getMessage(), e);
        }
    }

    @Override
    public String getPasswordById(final UserDetailsImpl userDetails) throws RepositoryException {
        try {
            return mapper.getPasswordById(userDetails);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting password by user_id: " + e.getMessage(), e);
        }
    }

    @Override
    public void updatePassword(final UserDetailsImpl userDetails) throws RepositoryException {
        try {
            mapper.updatePassword(userDetails);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while updating password: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<UserDetailsImpl> getUserById(Long id) throws RepositoryException {
        try {
            return Optional.ofNullable(mapper.getUserById(id));
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting user by id: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<UserDetailsImpl> getUserByEmail(String email) throws RepositoryException {
        try {
            return Optional.ofNullable(mapper.getUserByEmail(email));
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting user by email: " + e.getMessage(), e);
        }
    }

    @Override
    @Description("You can remove users by email or by id.")
    public void remove(final UserDetailsImpl user) throws RepositoryException {
        try {
            mapper.remove(user);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while removing user: " + e.getMessage(), e);
        }
    }

    @Override
    public void confirm(String email) throws RepositoryException {
        try {
            mapper.confirm(email);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while confirming email: " + e.getMessage(), e);
        }
    }

    @Override
    public void setTokenByEmail(String email, String token) throws RepositoryException {
        try {
            mapper.setTokenByEmail(email, token);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while setting token to user by email: " + e.getMessage(), e);
        }
    }

    @Override
    public String getTokenByEmail(String email) throws RepositoryException {
        try {
            return mapper.getTokenByEmail(email);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting token by email: " + e.getMessage(), e);
        }
    }
}
