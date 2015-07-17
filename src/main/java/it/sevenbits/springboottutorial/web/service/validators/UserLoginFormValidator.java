package it.sevenbits.springboottutorial.web.service.validators;


import it.sevenbits.springboottutorial.web.domain.UserLoginForm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by sevenbits on 16.07.15.
 */
@Service
public class UserLoginFormValidator implements Validator {
    private static final Logger LOG = Logger.getLogger(UserCreateFormValidator.class);

    @Autowired
    private CommonFieldValidator validator;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UserLoginForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserLoginForm form = (UserLoginForm) target;
        LOG.debug("Validating user with email = " + form.getEmail());

        //no tests for spec symbols
        validatePasswords(errors, form);
        validateEmail(errors, form);
    }

    private void validatePasswords(Errors errors, UserLoginForm form) {
        if (validator.isShorterThan(form.getPassword(), 255)) {
            errors.reject("password.length", "This password is too long.");
            return;
        }
    }

    private void validateEmail(Errors errors, UserLoginForm form) {
        try {
            //i don like this bunch of "if", but i think we should not use some pattern here
            if (validator.isEmail(form.getEmail())) {
                errors.reject("email.valid", "This email is incorrect.");
                return;
            }

            if (validator.isShorterThan(form.getEmail(), 255)) {
                errors.reject("email.length", "This email is too long.");
                return;
            }
        } catch (Exception e) {
            errors.reject("email.error", "Cant get email from data source. " + e.getMessage());
        }
    }
}
