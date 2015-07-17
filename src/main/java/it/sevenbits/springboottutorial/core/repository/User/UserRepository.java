package it.sevenbits.springboottutorial.core.repository.User;

import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.mappers.UserMapper;
import it.sevenbits.springboottutorial.core.repository.RepositoryException;
import org.apache.catalina.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
        if (userDetails == null) {
            throw new RepositoryException("User object is null.");
        }
        try {
            mapper.insert(userDetails);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while saving user: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isEmailExists(final UserDetailsImpl userDetails) throws RepositoryException {

        return mapper.isEmailExists(userDetails) == 0 ? false : true;
    }

    @Override
    public Long getIdByEmail(final UserDetailsImpl userDetails) throws RepositoryException {

        return mapper.getIdByEmail(userDetails) == null ? -1 : (Long) mapper.getIdByEmail(userDetails);
    }

    @Override
    public String getPasswordById(final UserDetailsImpl userDetails) throws RepositoryException {

        return mapper.getPasswordById(userDetails);
    }

    @Override
    public void updatePassword(final UserDetailsImpl userDetails) {

        mapper.updatePassword(userDetails);
    }

    @Override
    public Optional<UserDetailsImpl> getUserById(Long id) throws RepositoryException {
        return Optional.ofNullable(mapper.getUserById(id));
    }

    @Override
    public Optional<UserDetailsImpl> getUserByEmail(String email) throws RepositoryException {
        return Optional.ofNullable(mapper.getUserByEmail(email));
    }

    @Override
    public Optional<UserDetailsImpl> getUserByName(String name) throws RepositoryException {
        return Optional.ofNullable(mapper.getUserByName(name));
    }
}
