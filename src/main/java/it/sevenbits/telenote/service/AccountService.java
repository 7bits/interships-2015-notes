package it.sevenbits.telenote.service;

import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import it.sevenbits.telenote.core.repository.Account.IAccountRepository;
import it.sevenbits.telenote.core.repository.RepositoryException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Service
public class AccountService {
    private static final Logger LOG = Logger.getLogger(AccountService.class);
    @Autowired
    @Qualifier(value = "accountRepository")
    private IAccountRepository accountRepository;
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

    public AccountService() {
        this.customTx = new DefaultTransactionDefinition();
        this.customTx.setName(TX_NAME);
        this.customTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    }

    public void changeTheme(UserDetailsImpl user) throws ServiceException {
        TransactionStatus status = txManager.getTransaction(customTx);
        try {
            accountRepository.changeStyle(user);
            txManager.commit(status);
            LOG.info(String.format("User's theme is successfully updated. UserEmail: %s, Theme: %s", user.getUsername(), user.getStyle()));
        } catch (RepositoryException e) {
            LOG.error(String.format("Could not change user's theme. UserEmail: %s, Theme: %s", user.getUsername(),user.getStyle()));
            txManager.rollback(status);
            throw new ServiceException("Could not change user's theme." + e.getMessage());
        }
    }

    public void changeUsername(UserDetailsImpl user) throws ServiceException {
        TransactionStatus status = txManager.getTransaction(customTx);
        try {
            //Pattern pattern = Pattern.compile("\\b[\\wа-яА-Я-]{2,15}\\b");
            Pattern pattern = Pattern.compile(".+\\s.+");
            Matcher matcher = pattern.matcher(user.getName());
            if (!matcher.matches()) {
                accountRepository.changeUsername(user);
                txManager.commit(status);
                LOG.info(String.format("Username is successfully updated. UserEmail: %s, NewUserName: %s", user.getUsername(), user.getName()));
            } else {
                LOG.warn(String.format("Incorrect username. UserEmail: %s, NewUserName: %s", user.getUsername(), user.getName()));
                throw new ServiceException("incorrectUsername");
            }

        } catch (RepositoryException e) {
            LOG.error(String.format("Could not change username. UserEmail: %s, NewUserName: %s", user.getUsername(), user.getName()));
            txManager.rollback(status);
            throw new ServiceException("Could not change username: " + e.getMessage());
        }
    }

    public void changePass(String currentPass, String newPass, UserDetailsImpl user) throws ServiceException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        TransactionStatus status = txManager.getTransaction(customTx);
        try {
            Pattern pattern = Pattern.compile("(([a-z]+[A-Z]+[0-9]+)|([a-z]+[0-9]+[A-Z]+)|([A-Z]+[a-z]+[0-9]+)|([A-Z]+[0-9]+[a-z]+)|([0-9]+[A-Z]+[a-z]+)|([0-9]+[a-z]+[A-Z]+)|([0-9]+[a-z]+)|([a-z]+[0-9]+))");
            Matcher matcher = pattern.matcher(newPass);

            if(matcher.matches()) {
                if (encoder.matches(currentPass, user.getPassword())) {
                    if (!currentPass.equals(newPass)){
                        user.setPassword(encoder.encode(newPass));
                        accountRepository.changePass(user);
                        txManager.commit(status);
                        LOG.info("Password is successfully changed. UserEmail: " + user.getUsername());
                    } else {
                        throw new ServiceException("curPassEqualsNewPass");
                    }
                } else {
                    throw new ServiceException("incorrectPass");
                }

            } else {
                throw new ServiceException("patternFail");
            }
        } catch (RepositoryException e) {
            LOG.error(String.format("An error occurred while changing password. UserEmail: %s", user.getUsername()));
            txManager.rollback(status);
            throw new ServiceException("An error occurred while changing password: " + e.getMessage());
        }
    }
}
