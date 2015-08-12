package it.sevenbits.springboottutorial.web.service;

import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.repository.Account.IAccountRepository;
import it.sevenbits.springboottutorial.core.repository.RepositoryException;
import it.sevenbits.springboottutorial.web.domain.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    @Qualifier(value = "accountRepository")
    private IAccountRepository accountRepository;

    public void chacgeTheme(UserDetailsImpl user) throws ServiceException {
        try {
            accountRepository.changeTheme(user);
        } catch (RepositoryException e) {
            throw new ServiceException("Ошибка смены стиля: " + e.getMessage());
        }
    }

    public ResponseEntity<ResponseMessage> changeUsername(UserDetailsImpl user) throws ServiceException {
        try {
            accountRepository.changeUsername(user);

            return new ResponseEntity(new ResponseMessage(true, "Имя изменено"), HttpStatus.OK);
        } catch (RepositoryException e) {
            throw new ServiceException("Не удалось изменить имя в базе: " + e.getMessage());
        }
    }

    public ResponseEntity<ResponseMessage> changePass(String oldPass, String newPass, UserDetailsImpl user) throws ServiceException {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        newPass = encoder.encode(newPass);

        try {
            if(encoder.matches(oldPass, user.getPassword())) {
                user.setPassword(newPass);
                accountRepository.changePass(user);
                return new ResponseEntity<>(new ResponseMessage(true, "Пароль сменён"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseMessage(false, "Старый пароль не верен!"), HttpStatus.CONFLICT);
            }
        } catch (RepositoryException e) {
            throw new ServiceException("Не удалось сменить пароль в базе: " + e.getMessage());
        }
    }
}