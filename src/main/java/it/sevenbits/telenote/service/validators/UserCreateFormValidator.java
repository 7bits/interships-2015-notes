package it.sevenbits.telenote.service.validators;

import it.sevenbits.telenote.service.UserService;
import it.sevenbits.telenote.web.domain.forms.UserCreateForm;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validates user email for registration.
 */
@Service
public class UserCreateFormValidator implements Validator {

    private static final Logger LOG = Logger.getLogger(UserCreateFormValidator.class);

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UserCreateForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserCreateForm form = (UserCreateForm) target;
        LOG.debug("Validating user with email = " + form.getEmail());

        validateEmail(errors, form);
    }

    private void validateEmail(Errors errors, UserCreateForm form) {
        try {
            if (userService.getUserByEmail(form.getEmail().toLowerCase()).isPresent()) {
                errors.reject("email.exists", "Пользователь с таким почтовым адресом уже существует.");
                return;
            }
        } catch (Exception e) {
            errors.reject("email.error", "Cant get email from data source. " + e.getMessage());
        }
    }
}
