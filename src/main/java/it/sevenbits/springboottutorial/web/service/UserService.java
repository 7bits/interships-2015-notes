package it.sevenbits.springboottutorial.web.service;

import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.domain.Role;
import it.sevenbits.springboottutorial.core.repository.User.IUserRepository;

import it.sevenbits.springboottutorial.web.domain.UserCreateForm;
import it.sevenbits.springboottutorial.web.domain.UserLoginForm;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
        userDetails.setPassword(form.getPassword());
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
            Optional<UserDetailsImpl> userDetails = this.getUserByEmail(email);
            if (userDetails.isPresent() && userDetails.get().getRole().equals(Role.ROLE_USER)) {
                return userDetails.get();
            }
        } catch (Exception e) {
            LOG.error("Cant load user by username due to repository error: " + e.getMessage(), e);
            throw new UsernameNotFoundException("User details can not be obtained because of " + e.getMessage(), e);
        }

        LOG.info("Cannot load user by username because there are no user details for this username.");
        throw new UsernameNotFoundException("There are no user details for this username");
    }

    public Long signIn(final UserLoginForm form) throws ServiceException {
        final UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setEmail(form.getEmail());
        userDetails.setPassword(form.getPassword());

        try {
            Long id = repository.getIdByEmail(userDetails);

            if (id < 0)
                throw new ServiceException("Incorrect username or password!");

            userDetails.setId(id);

            String password = repository.getPasswordById(userDetails);

            return userDetails.getPassword().equals(password) ? id : -1;
        } catch (Exception e) {
            throw new ServiceException("An error occurred while sign in user: " + e.getMessage(), e);
        }
    }

    public void updatePassword(final UserCreateForm form, String password) throws ServiceException {

        final UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setEmail(form.getEmail());
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

    public Optional<UserDetailsImpl> getUserByName(String name) throws ServiceException {
        try {
            return repository.getUserByName(name);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
    /*public List<SubscriptionModel> findAll() throws ServiceException {
        try {
            List<Subscription> subscriptions = repository.findAll();
            List<SubscriptionModel> models = new ArrayList<>(subscriptions.size());
            for (Subscription s: subscriptions) {
                models.add(new SubscriptionModel(
                        s.getId(),
                        s.getName(),
                        s.getEmail()
                ));
            }

            return models;
        } catch (Exception e) {
            throw new ServiceException("An error occurred while retrieving subscriptions: " + e.getMessage(), e);
        }
    }*/
}
