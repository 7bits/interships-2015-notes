package it.sevenbits.telenote.service;

import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import it.sevenbits.telenote.core.repository.Account.IAccountRepository;
import it.sevenbits.telenote.core.repository.RepositoryException;
import it.sevenbits.telenote.web.domain.forms.ChangesForm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Service
public class AccountService {
    private static final Logger LOG = Logger.getLogger(AccountService.class);
    @Autowired
    @Qualifier(value = "accountRepository")
    private IAccountRepository accountRepository;

    @Autowired
    private MessageSource messageSource;

    /**Transaction settings name */
    private static final String TX_NAME = "txService";

    /** Spring Transaction Manager */
    @Autowired
    private PlatformTransactionManager txManager;

    /** Transaction settings object */
    private DefaultTransactionDefinition customTx;

    public AccountService() {
        this.customTx = new DefaultTransactionDefinition();
        this.customTx.setName(TX_NAME);
        this.customTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    /**
     * Updates user theme by user id.
     *
     * @param user POJO that contains theme name and user id.
     * @throws ServiceException
     */
    public Map<String, Object> changeTheme(UserDetailsImpl user) throws ServiceException {
        TransactionStatus status = null;
        Map<String, Object> map = new HashMap<>();
        try {
            status = txManager.getTransaction(customTx);
            accountRepository.changeStyle(user);
            txManager.commit(status);
            map.put("themeChanged", messageSource.getMessage("message.theme.themechanged", null, LocaleContextHolder.getLocale()));
            LOG.info(String.format("User's theme is successfully updated. UserEmail: %s, Theme: %s", user.getUsername(), user.getStyle()));
            return map;
        } catch (RepositoryException e) {
            LOG.error(String.format("Could not change user's theme. UserEmail: %s, Theme: %s", user.getUsername(), user.getStyle()));
            if (status != null) {
                txManager.rollback(status);
                LOG.info("Rollback done.");
            }
            throw new ServiceException("Could not change user's theme." + e.getMessage());
        }
    }

    /**
     * Validates and updates username by user id.
     *
     * @param user POJO that     contains username and user id.
     * @throws ServiceException
     */
    public Map<String, Object> changeUsername(UserDetailsImpl user) throws ServiceException {
        TransactionStatus status = null;
        Map<String, Object> map = new HashMap<>();
        try {
            //Pattern pattern = Pattern.compile("\\b[\\wа-яА-Я-]{2,15}\\b");
            Pattern pattern = Pattern.compile(".+\\s.+");
            Matcher matcher = pattern.matcher(user.getName());
            if (!matcher.matches()) {
                status = txManager.getTransaction(customTx);
                accountRepository.changeUsername(user);
                txManager.commit(status);
                map.put("usernameChanged", messageSource.getMessage("message.username.usernamechanged", null, LocaleContextHolder.getLocale()));
                LOG.info(String.format("Username is successfully updated. UserEmail: %s, NewUserName: %s", user.getUsername(), user.getName()));
            } else {
                LOG.warn(String.format("Incorrect username. UserEmail: %s, NewUserName: %s", user.getUsername(), user.getName()));
                map.put("incorrectUsername", messageSource.getMessage("message.username.incorrectusername", null, LocaleContextHolder.getLocale()));
            }
            return map;
        } catch (RepositoryException e) {
            LOG.error(String.format("Could not change username. UserEmail: %s, NewUserName: %s", user.getUsername(), user.getName()));
            if (status != null) {
                txManager.rollback(status);
                LOG.info("Rollback done.");
            }
            throw new ServiceException("Could not change username: " + e.getMessage());
        }
    }


    /**
     * Validates and updates user password by user id.
     *
     * @param currentPass current user password.
     * @param newPass     new user password.
     * @param user        POJO that contains user password and user id.
     * @throws ServiceException
     */
    public Map<String, Object> changePass(String currentPass, String newPass, UserDetailsImpl user) throws ServiceException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        TransactionStatus status = null;
        Map<String, Object> map = new HashMap<>();

        try {
            status = txManager.getTransaction(customTx);
            // (?=.*[0-9])(?=.*[a-zA-Z]).{6,20}
            Pattern pattern = Pattern.compile("(([a-z]+[A-Z]+[0-9]+)|([a-z]+[0-9]+[A-Z]+)|([A-Z]+[a-z]+[0-9]+)|([A-Z]+[0-9]+[a-z]+)|([0-9]+[A-Z]+[a-z]+)|([0-9]+[a-z]+[A-Z]+)|([0-9]+[a-z]+)|([a-z]+[0-9]+))");
            Matcher matcher = pattern.matcher(newPass);

            if (matcher.matches()) {
                if (encoder.matches(currentPass, user.getPassword())) {
                    if (!currentPass.equals(newPass)) {
                        user.setPassword(encoder.encode(newPass));
                        accountRepository.changePass(user);
                        map.put("passChanged", messageSource.getMessage("message.password.passchanged", null, LocaleContextHolder.getLocale()));
                    } else {
                        map.put("curPassEqualsNewPass", messageSource.getMessage("message.password.curpassequalsnewpass", null, LocaleContextHolder.getLocale()));
                    }
                } else {
                    map.put("incorrectPass", messageSource.getMessage("message.password.incorrectpass", null, LocaleContextHolder.getLocale()));
                }

            } else {
                map.put("patternFail", messageSource.getMessage("message.password.patternfail", null, LocaleContextHolder.getLocale()));
            }
            txManager.commit(status);
            LOG.info("Password is successfully changed. UserEmail: " + user.getUsername());
            return map;
        } catch (RepositoryException e) {
            LOG.error(String.format("An error occurred while changing password. UserEmail: %s", user.getUsername()));
            if (status != null) {
                txManager.rollback(status);
                LOG.info("Rollback done.");
            }
            throw new ServiceException("An error occurred while changing password: " + e.getMessage());
        }
    }

    public Map<String, Object> changeAccountSettings(ChangesForm form, UserDetailsImpl user) throws ServiceException {
        Map<String, Object> map = new HashMap<>();

        if (!form.getUsername().equals(user.getName())) {
            user.setName(form.getUsername());
            Map<String, Object> usernameMap = changeUsername(user);
            map.putAll(usernameMap);
        }

        if (!form.getStyle().equals(user.getStyle())) {
            user.setStyle(form.getStyle());
            Map<String, Object> themeMap = changeTheme(user);
            map.putAll(themeMap);
        }

        if (!(form.getNewPass().isEmpty() && form.getCurrentPass().isEmpty())) {
            Map<String, Object> passMap = changePass(form.getCurrentPass(), form.getNewPass(), user);
            map.putAll(passMap);
        }
        return map;
    }
}
