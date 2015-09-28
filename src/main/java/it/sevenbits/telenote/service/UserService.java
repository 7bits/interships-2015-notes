package it.sevenbits.telenote.service;

import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import it.sevenbits.telenote.core.domain.Role;
import it.sevenbits.telenote.core.repository.RepositoryException;
import it.sevenbits.telenote.core.repository.User.IUserRepository;

import it.sevenbits.telenote.web.domain.forms.UserCreateForm;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.apache.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;

import java.security.SecureRandom;
import java.util.*;

@Service
public class UserService implements UserDetailsService {
    private static Logger LOG = Logger.getLogger(UserService.class);

    @Autowired
    @Qualifier(value = "theUserPersistRepository")
    private IUserRepository repository;

    @Autowired
    private EmailService emailService;
    /**
     * Transaction settings name
     */
    private static final String TX_NAME = "txService";
    /**
     * Spring Transaction Manager
     */
    @Autowired
    private PlatformTransactionManager txManager;
    /**
     * Transaction settings object
     */
    private DefaultTransactionDefinition customTx;

    public UserService() {
        this.customTx = new DefaultTransactionDefinition();
        this.customTx.setName(TX_NAME);
        this.customTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    public void create(final UserDetailsImpl user) throws ServiceException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        TransactionStatus status = null;
        try {
            status = txManager.getTransaction(customTx);
            if (repository.isEmailExists(user))
                throw new ServiceException("Sorry, e-mail is already exists");

            repository.create(user);
            txManager.commit(status);
            LOG.info(String.format("New user is created: UserId: %d, UserEmail: %s.", user.getId(), user.getUsername()));
        } catch (Exception e) {
            LOG.error(String.format("An error occurred while creating user. UserId: %d, UserEmail: %s.",
            user.getId(), user.getName()));
            if (status != null) {
                txManager.rollback(status);
                LOG.info("Rollback done.");
            }
            throw new ServiceException("An error occurred while creating user: " + e.getMessage(), e);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        TransactionStatus status = null;
        try {
            status = txManager.getTransaction(customTx);
            Optional<UserDetailsImpl> userDetails = this.getUserByEmail(email.toLowerCase());
            if (userDetails.isPresent() && userDetails.get().getRole().equals(Role.USER)) {
                //if (userDetails.isPresent() && userDetails.get().getRole().equals(Role.USER) && userDetails.get().getIsConfirmed()) {
                return userDetails.get();
            }
        } catch (Exception e) {
            LOG.error("Cant load user by username. UserEmail: " + email);
            throw new UsernameNotFoundException("User details can not be obtained because of " + e.getMessage(), e);
        }

        LOG.info("Cannot load user by username because there are no user details for this username.");
        throw new UsernameNotFoundException("There are no user details for this username");
    }

    public void updatePassword(final UserCreateForm form, String password) throws ServiceException {

        final UserDetailsImpl user = new UserDetailsImpl();
        user.setUsername(form.getEmail().toLowerCase());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(password));

        TransactionStatus status = null;
        try {
            status = txManager.getTransaction(customTx);
            repository.updatePassword(user);
            txManager.commit(status);
            LOG.info("Password is updated. UserEmail: " + user.getUsername());
        } catch (Exception e) {
            LOG.error("An error occurred while updating password. UserEmail: " + user.getUsername());
            if (status != null) {
                txManager.rollback(status);
                LOG.info("Rollback done.");
            }
            throw new ServiceException("E-mail does not exist!");
        }
    }

    public Optional<UserDetailsImpl> getUserByEmail(String email) throws ServiceException {
        Optional<UserDetailsImpl> user = null;
        TransactionStatus status = null;
        try {
            status = txManager.getTransaction(customTx);
            user = repository.getUserByEmail(email);
            txManager.commit(status);
            return user;
        } catch (Exception e) {
            //mb, has to be reworked
            LOG.error("Could not get user by email. UserEmail: " + email);
            if (status != null) {
                txManager.rollback(status);
                LOG.info("Rollback done.");
            }

            throw new ServiceException(e.getMessage());
        }
    }

    public Optional<UserDetailsImpl> getUserById(Long userId) throws ServiceException {
        Optional<UserDetailsImpl> user = null;
        TransactionStatus status = null;
        try {
            status = txManager.getTransaction(customTx);
            user = repository.getUserById(userId);
            txManager.commit(status);
            return user;
        } catch (Exception e) {
            LOG.error("Could not get user by id. UserId: " + userId);
            if (status != null) {
                txManager.rollback(status);
                LOG.info("Rollback done.");
            }
            throw new ServiceException(e.getMessage());
        }
    }

    public void confirm(String email) throws ServiceException {
        TransactionStatus status = null;
        try {
            status = txManager.getTransaction(customTx);
            repository.confirm(email);
            txManager.commit(status);
            LOG.info("Email is confirmed. UserEmail: " + email);
        } catch (Exception e) {
            LOG.error("Could not confirm email. UserEmail: " + email);
            if (status != null) {
                txManager.rollback(status);
                LOG.info("Rollback done.");
            }
            throw new ServiceException(e.getMessage());
        }
    }

    public String getToken(String email) throws ServiceException {
        String token = "";
        TransactionStatus status = null;
        try {
            status = txManager.getTransaction(customTx);
            token = repository.getTokenByEmail(email);
            txManager.commit(status);
            return token;
        } catch (Exception e) {
            LOG.error("Could not get token by email. UserEmail: " + email);
            if (status != null) {
                txManager.rollback(status);
                LOG.info("Rollback done.");
            }
            throw new ServiceException(e.getMessage());
        }
    }

    public String setNewToken(String email) throws ServiceException {
        String token = RandomStringUtils.random(32, 0, 0, true, true, null, new SecureRandom());
        TransactionStatus status = null;
        try {
            status = txManager.getTransaction(customTx);
            repository.setTokenByEmail(email, token);
            txManager.commit(status);
        } catch (RepositoryException ex) {
            LOG.error("Could not set new token. User Email: " + email);
            if (status != null) {
                txManager.rollback(status);
                LOG.info("Rollback done.");
            }
            throw new ServiceException(ex.getMessage());
        }

        return token;
    }

    public Map<String, String> getSignUpErrors(BindingResult bindingResult) throws ServiceException {
        Map<String, String> matcher = new HashMap<>();

        for (ObjectError objectError : bindingResult.getAllErrors()) {
            try {
                String errCod = objectError.getCode();
                if (!matcher.containsKey(errCod)) {
                    matcher.put(errCod, ((FieldError) objectError).getField().toString());
                }
            } catch (Exception e) {
                LOG.error("Wrong type of error, expected FieldError, get another. " + e.getMessage());
                throw new ServiceException(e.getMessage());
            }
        }
        return matcher;
    }

    public boolean resetPassInDB(String email) throws ServiceException {
        TransactionStatus status = null;
        try {
            status = txManager.getTransaction(customTx);
            Optional<UserDetailsImpl> user = getUserByEmail(email);
            if (user.isPresent()) {
                String token = setNewToken(user.get().getUsername());
                String link = "http://tele-notes.7bits.it/resetpass?token=" + token + "&email=" + email;

                HashMap<String, Object> map = new HashMap<>();
                map.put("link", link);
                map.put("username", user.get().getName());

                emailService.sendHtml(user.get().getUsername(), "Tele-notes. Восстановление пароля.", "mails/changePassMail", map);
                txManager.commit(status);
                LOG.info("Password is successfully reset. UserEmail: " + email);
            } else {
                return false;
            }
        } catch (ServiceException e) {
            LOG.error("Could not reset password. UserEmail: " + email);
            if (status != null) {
                txManager.rollback(status);
                LOG.info("Rollback done.");
            }
            throw new ServiceException("Could not reset password. UserEmail: " + email, e);
        }
        return true;
    }

    public boolean updatePass(String email, String token, String[] passwords) throws ServiceException {
        TransactionStatus status = null;
        try {
            status = txManager.getTransaction(customTx);
            Optional<UserDetailsImpl> user = getUserByEmail(email);
            if (user.isPresent() && token.equals(getToken(email)) &&
            passwords[0].equals(passwords[1])) {

                UserCreateForm userForm = new UserCreateForm();
                userForm.setEmail(email);

                updatePassword(userForm, passwords[0]);
                txManager.commit(status);
                return true;
            } else {
                return false;
            }
        } catch (ServiceException e) {
            LOG.error("Could not update password. UserEmail: " + email);
            if (status != null) {
                txManager.rollback(status);
                LOG.info("Rollback done.");
            }
            throw new ServiceException("Could not update password. UserEmail: " + email, e);
        }
    }

    public void cleanDB() throws ServiceException {
        TransactionStatus status = null;
        try {
            status = txManager.getTransaction(customTx);
            repository.cleanDB();
            txManager.commit(status);
        } catch (RepositoryException e) {
            LOG.error("Could not clean database.");
            if (status != null) {
                txManager.rollback(status);
                LOG.info("Rollback done.");
            }
            throw new ServiceException("Could not clean database.", e);
        }
    }
}
