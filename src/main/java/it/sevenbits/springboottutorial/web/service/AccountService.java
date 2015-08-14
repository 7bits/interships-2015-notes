package it.sevenbits.springboottutorial.web.service;

import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.repository.Account.IAccountRepository;
import it.sevenbits.springboottutorial.core.repository.RepositoryException;
import it.sevenbits.springboottutorial.web.domain.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Service
public class AccountService {

    @Autowired
    @Qualifier(value = "accountRepository")
    private IAccountRepository accountRepository;

    public void changeTheme(UserDetailsImpl user) throws ServiceException {
        try {
            accountRepository.changeTheme(user);
        } catch (RepositoryException e) {
            throw new ServiceException("Ошибка смены стиля: " + e.getMessage());
        }
    }

    public void changeUsername(UserDetailsImpl user) throws ServiceException {
        try {
            Pattern pattern = Pattern.compile(".+\\s.+");
            Matcher matcher = pattern.matcher(user.getUsername());
            if (!matcher.find(0)) {
                accountRepository.changeUsername(user);
            } else {
                throw new ServiceException("username");
            }

        } catch (RepositoryException e) {
            throw new ServiceException("Не удалось изменить имя в базе: " + e.getMessage());
        }
    }

    public void changePass(String oldPass, String newPass, UserDetailsImpl user) throws ServiceException {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (oldPass.equals(newPass)) throw new ServiceException("password");

        try {

            Pattern pattern = Pattern.compile("(([a-z]+[A-Z]+[0-9]+)|([a-z]+[0-9]+[A-Z]+)|([A-Z]+[a-z]+[0-9]+)|([A-Z]+[0-9]+[a-z]+)|([0-9]+[A-Z]+[a-z]+)|([0-9]+[a-z]+[A-Z]+)|([0-9]+[a-z]+)|([a-z]+[0-9]+))");
            Matcher matcher = pattern.matcher(newPass);

            if(matcher.matches()) {
                if (encoder.matches(oldPass, user.getPassword())) {
                    user.setPassword(encoder.encode(newPass));
                    accountRepository.changePass(user);
                } else {
                    throw new ServiceException("oldPass");
                }

            } else {
                throw new ServiceException("newPass");
            }
        } catch (RepositoryException e) {
            throw new ServiceException("Не удалось сменить пароль в базе: " + e.getMessage());
        }
    }

    public String getAvatarHash(String email) {
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();

        return encoder.encodePassword(email, null);
    }
}
