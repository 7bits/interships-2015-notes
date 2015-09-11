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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private static Logger LOG = Logger.getLogger(UserService.class);

    @Autowired
    @Qualifier(value = "theUserPersistRepository")
    private IUserRepository repository;

    @Autowired
    private EmailService emailService;

    public void create(final UserDetailsImpl user) throws ServiceException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));

        try {
            if (repository.isEmailExists(user))
                throw new ServiceException("Sorry, e-mail is already exists");

            repository.create(user);
            LOG.debug(String.format("New user created: %s, %d", user, user.getId() ));
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
        userDetails.setUsername(form.getEmail().toLowerCase());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userDetails.setPassword(encoder.encode(password));

        try {
            repository.updatePassword(userDetails);
        } catch (Exception e) {
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

    public ModelAndView getSignUpErrors(String url, BindingResult bindingResult) throws ServiceException {
        ModelAndView model = new ModelAndView(url);
        List<String> matcher = new ArrayList<String>();

        for (ObjectError objectError : bindingResult.getAllErrors()) {
            try {
                String error = ((FieldError) objectError).getField().toString();
                if(!matcher.contains(error)) {
                    matcher.add(error);
                    model.addObject(error + "Error", error);
                }
            } catch (Exception e) {
                model.addObject("emailExists", objectError);
            }
        }
        return model;
    }

    public ModelAndView resetPassInDB(String email) throws ServiceException {
        Optional<UserDetailsImpl> user = getUserByEmail(email);
        if (user.isPresent()) {
            String token = setNewToken(user.get().getUsername());
            String link = "http://tele-notes.7bits.it/resetpass?token=" + token + "&email=" + email;

            HashMap<String, Object> map = new HashMap<>();
            map.put("link", link);
            map.put("username", user.get().getName());

            emailService.sendHtml(user.get().getUsername(), "Tele-notes. Восстановление пароля.", "home/changePassMail", map);
        } else {
            return new ModelAndView("home/resetPass", "error", email);
        }

        return null;
    }

    public ModelAndView updatePass(String email, String token, String[] passwords) throws ServiceException {
        Optional<UserDetailsImpl> user = getUserByEmail(email);
        if (user.isPresent() && token.equals(getToken(email))
                && passwords[0].equals(passwords[1])) {

            UserCreateForm userForm = new UserCreateForm();
            userForm.setEmail(email);

            updatePassword(userForm, passwords[0]);

            return null;
        } else {
            return new ModelAndView("home/newpass");
        }
    }
}
