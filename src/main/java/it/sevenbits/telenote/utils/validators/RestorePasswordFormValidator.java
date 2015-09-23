package it.sevenbits.telenote.utils.validators;

import it.sevenbits.telenote.web.domain.forms.RestorePasswordForm;
import org.apache.log4j.Logger;
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

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(RestorePasswordForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RestorePasswordForm form = (RestorePasswordForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "message.validate.email.notempty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "message.validate.password.notempty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordRepeat", "message.validate.password.notempty");

        CommonFieldValidator validator = new CommonFieldValidator();
        if (!validator.isEmail(form.getEmail())) {
            LOG.info("Someone trying to restore wrong email = " + form.getEmail());
            errors.reject("message.validate.email.correct");
        }

        if (!validator.isInRange(form.getEmail().length(), 1, 255)) {
            LOG.info("Someone trying to restore email not in length range 1,255 = " + form.getEmail());
            errors.reject("message.validate.email.length");
        }

        if (!validator.isInRange(form.getPassword().length(), 5, 255)) {
            LOG.info("Someone trying to input password not in length range 5,255 = " + form.getPassword());
            errors.reject("message.validate.password.length");
        }

        if (!validator.isInRange(form.getPasswordRepeat().length(), 5, 255)) {
            LOG.info("Someone trying to restore password not in length range 5,255 = " + form.getPasswordRepeat());
            errors.reject("message.validate.password.length");
        }
    }
}
