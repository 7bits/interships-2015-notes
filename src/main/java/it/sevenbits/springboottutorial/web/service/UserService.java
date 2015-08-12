package it.sevenbits.springboottutorial.web.service;

import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.domain.Role;
import it.sevenbits.springboottutorial.core.repository.RepositoryException;
import it.sevenbits.springboottutorial.core.repository.User.IUserRepository;

import it.sevenbits.springboottutorial.web.domain.UserCreateForm;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.apache.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private static Logger LOG = Logger.getLogger(UserService.class);

    @Autowired
    @Qualifier(value = "theUserPersistRepository")
    private IUserRepository repository;

    public void create(final UserCreateForm form) throws ServiceException {
        final UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setEmail(form.getEmail());
        userDetails.setUsername(form.getUsername());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userDetails.setPassword(encoder.encode(form.getPassword()));

        try {
            if (repository.isEmailExists(userDetails))
                throw new ServiceException("Sorry, e-mail is already exists");

            repository.create(userDetails);
            LOG.debug(String.format("New user created: %s, %d", userDetails, userDetails.getId() ));
        } catch (Exception e) {
            throw new ServiceException("An error occurred while saving subscription: " + e.getMessage(), e);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            LOG.info("Loading user by email: " + email);
            Optional<UserDetailsImpl> userDetails = this.getUserByEmail(email.toLowerCase());
            if (userDetails.isPresent() && userDetails.get().getRole().equals(Role.USER)) {
            //if (userDetails.isPresent() && userDetails.get().getRole().equals(Role.USER) && userDetails.get().getIsConfirmed()) {
                return userDetails.get();
            }
        } catch (Exception e) {
            LOG.error("Cant load user by username due to repository error: " + e.getMessage(), e);
            throw new UsernameNotFoundException("User details can not be obtained because of " + e.getMessage(), e);
        }

        LOG.info("Cannot load user by username because there are no user details for this username.");
        throw new UsernameNotFoundException("There are no user details for this username");
    }

    public void updatePassword(final UserCreateForm form, String password) throws ServiceException {

        final UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setEmail(form.getEmail().toLowerCase());
        userDetails.setPassword(password);

        try {
            if (repository.isEmailExists(userDetails)) {
                userDetails.setId(repository.getIdByEmail(userDetails));
                repository.updatePassword(userDetails);
            } else {
                throw new ServiceException("E-mail is not exists!");
            }
        }
        catch (Exception e) {
            throw new ServiceException("E-mail is not exists!");
        }
    }

    public Optional<UserDetailsImpl> getUserByEmail(String email) throws ServiceException {
        try {
            return repository.getUserByEmail(email);
        } catch (Exception e) {
            //mb, has to be reworked
            throw new ServiceException(e.getMessage());
        }
    }

    public Optional<UserDetailsImpl> getUserById(Long id) throws ServiceException {
        try {
            return repository.getUserById(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void confirm(String email) throws ServiceException {
        try {
            repository.confirm(email);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public String getToken(String email) throws ServiceException {
        try {
            return repository.getTokenByEmail(email);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public String setNewToken(String email) throws ServiceException {
        String token = RandomStringUtils.random(32, 0, 0, true, true, null, new SecureRandom());

        try {
            repository.setTokenByEmail(email, token);
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage());
        }

        return token;
    }
}
