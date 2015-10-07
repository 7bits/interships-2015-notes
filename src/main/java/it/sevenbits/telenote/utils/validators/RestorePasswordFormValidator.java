package it.sevenbits.telenote.utils.validators;

import it.sevenbits.telenote.web.domain.forms.RestorePasswordForm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by sevenbits on 22.09.15.
 */
@Service
public class RestorePasswordFormValidator implements Validator {

    private static final Logger LOG = Logger.getLogger(RestorePasswordFormValidator.class);

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(RestorePasswordForm.class);
    }

    @Autowired
    private CommonFieldValidator validator;

    @Override
    public void validate(Object target, Errors errors) {
        RestorePasswordForm form = (RestorePasswordForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", messageSource.getMessage("message.validate.email.notempty", null, LocaleContextHolder.getLocale()));
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", messageSource.getMessage("message.validate.password.notempty", null, LocaleContextHolder.getLocale()));
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordRepeat", messageSource.getMessage("message.validate.password.notempty", null, LocaleContextHolder.getLocale()));

        if (!validator.isEmail(form.getEmail())) {
            LOG.info("Someone trying to restore wrong email = " + form.getEmail());
            errors.rejectValue("email", messageSource.getMessage("message.validate.email.correct", null, LocaleContextHolder.getLocale()));
        }

        if (!validator.isInRange(form.getEmail().length(), 1, 255)) {
            LOG.info("Someone trying to restore email not in length range 1,255 = " + form.getEmail());
            errors.rejectValue("email", messageSource.getMessage("message.validate.email.length", null, LocaleContextHolder.getLocale()));
        }

        if (!validator.isInRange(form.getPassword().length(), 5, 255)) {
            LOG.info("Someone trying to input password not in length range 5,255 = " + form.getPassword());
            errors.rejectValue("password", messageSource.getMessage("message.validate.password.length", null, LocaleContextHolder.getLocale()));
        }

        if (!validator.isInRange(form.getPasswordRepeat().length(), 5, 255)) {
            LOG.info("Someone trying to restore password not in length range 5,255 = " + form.getPasswordRepeat());
            errors.rejectValue("passwordRepeat", messageSource.getMessage("message.validate.password.length", null, LocaleContextHolder.getLocale()));
        }
    }
}
