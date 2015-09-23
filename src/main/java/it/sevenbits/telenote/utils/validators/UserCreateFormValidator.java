package it.sevenbits.telenote.utils.validators;

import it.sevenbits.telenote.web.domain.forms.UserCreateForm;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class UserCreateFormValidator implements Validator {

    private static final Logger LOG = Logger.getLogger(UserCreateFormValidator.class);

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UserCreateForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserCreateForm form = (UserCreateForm) target;
        LOG.debug("Validating user with email = " + form.getEmail());

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", messageSource.getMessage("message.validate.email.notempty", null, LocaleContextHolder.getLocale()));
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", messageSource.getMessage("message.validate.username.notempty", null, LocaleContextHolder.getLocale()));
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", messageSource.getMessage("message.validate.password.notempty", null, LocaleContextHolder.getLocale()));

        CommonFieldValidator validator = new CommonFieldValidator();
        if (!validator.isEmail(form.getEmail())) {
            LOG.info("Someone trying to input wrong email = " + form.getEmail());
            errors.rejectValue("email", messageSource.getMessage("message.validate.email.correct", null, LocaleContextHolder.getLocale()));
        }

        if (!validator.isInRange(form.getEmail().length(), 1, 255)) {
            LOG.info("Someone trying to input email not in length range 1,255 = " + form.getEmail());
            errors.rejectValue("email", messageSource.getMessage("message.validate.email.length", null, LocaleContextHolder.getLocale()));
        }

        if (!validator.isInRange(form.getUsername().length(), 1, 255)) {
            LOG.info("Someone trying to input username not in length range 1,255 = " + form.getUsername());
            errors.rejectValue("username", messageSource.getMessage("message.validate.username.length", null, LocaleContextHolder.getLocale()));
        }

        if (!validator.isInRange(form.getPassword().length(), 5, 255)) {
            LOG.info("Someone trying to input password not in length range 5,255 = " + form.getPassword());
            errors.rejectValue("password", messageSource.getMessage("message.validate.password.length", null, LocaleContextHolder.getLocale()));
        }
        //validateEmail(errors, form);
    }

    /*private void validateEmail(Errors errors, UserCreateForm form) {
        try {
            if (userService.getUserByEmail(form.getEmail()).isPresent()) {
                errors.reject("email.exists", messageSource.getMessage("message.signup.exist", null, LocaleContextHolder.getLocale()));
                return;
            }
        } catch (Exception e) {
            errors.reject("email.error", "Cant get email from data source. " + e.getMessage());
        }
    }*/
}