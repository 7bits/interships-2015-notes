package it.sevenbits.springboottutorial.web.service.validators;

import it.sevenbits.springboottutorial.web.domain.UserCreateForm;
import it.sevenbits.springboottutorial.web.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/*@Service
public class UserFormValidator {
    @Autowired
    private CommonFieldValidator validator;

    private static final Logger LOG = Logger.getLogger(UserFormValidator.class);

    public HashMap<String, String> validate(final UserForm form) {
        LOG.info("SubscriptionFormValidator started for: " + form.toString());
        HashMap<String, String> errors = new HashMap<>();

        validator.isNotNullOrEmpty(form.getEmail(), errors, "email", "Поле не должно быть пустым");
        validator.isNotNullOrEmpty(form.getUsername(), errors, "name", "Поле не должно быть пустым");

        validator.isEmail(form.getEmail(), errors, "email", "Введите правильный email");

        validator.shorterThan(form.getEmail(), 255, errors, "email", "Поле должно быть кроче чем 255 символов");
        validator.shorterThan(form.getUsername(), 255, errors, "name", "Поле должно быть кроче чем 255 символов");

        for (Map.Entry<String, String> entry : errors.entrySet()) {
            LOG.info(String.format("Error found: Filed=%s, Error=%s",
                    entry.getKey(), entry.getValue()));
        }

        return errors;
    }
}*/

@Service
public class UserCreateFormValidator implements Validator {

    private static final Logger LOG = Logger.getLogger(UserCreateFormValidator.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CommonFieldValidator validator;

    /*@Autowired
    public UserCreateFormValidator(UserService userService, CommonFieldValidator validator) {
        this.userService = userService;
        this.validator = validator;
    }*/

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UserCreateForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserCreateForm form = (UserCreateForm) target;
        LOG.debug("Validating user with email = " + form.getEmail());

        //no tests for spec symbols
        validateUsername(errors, form);
        validatePasswords(errors, form);
        validateEmail(errors, form);
    }

    private void validateUsername(Errors errors, UserCreateForm form) {
        if (!validator.isShorterThan(form.getUsername(), 255)) {
            errors.reject("username.length", "This username is too long.");
            return;
        }
    }

    private void validatePasswords(Errors errors, UserCreateForm form) {
        if (!validator.isShorterThan(form.getPassword(), 255)) {
            errors.reject("password.length", "This password is too long.");
            return;
        }

        if (!form.getPassword().equals(form.getPasswordRepeat())) {
            errors.reject("password.no_match", "Passwords do not match");
            return;
        }
    }

    private void validateEmail(Errors errors, UserCreateForm form) {
        try {
            //i don like this bunch of "if", but i think we should not use some pattern here
            if (!validator.isEmail(form.getEmail())) {
                errors.reject("email.valid", "This email is incorrect.");
                return;
            }

            if (!validator.isShorterThan(form.getEmail(), 255)) {
                errors.reject("email.length", "This email is too long.");
                return;
            }

            if (userService.getUserByEmail(form.getEmail()).isPresent()) {
                errors.reject("email.exists", "User with this email already exists.");
                return;
            }
        } catch (Exception e) {
            errors.reject("email.error", "Cant get email from data source. " + e.getMessage());
        }
    }
}