package it.sevenbits.telenote.utils.validators;

import it.sevenbits.telenote.web.domain.forms.ResetPassForm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by sevenbits on 02.10.15.
 */
@Service
public class ResetPasswordFormValidator implements Validator {
    private static final Logger LOG = Logger.getLogger(ResetPasswordFormValidator.class);

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(ResetPassForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ResetPassForm form = (ResetPassForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", messageSource.getMessage("message.validate.email.notempty", null, LocaleContextHolder.getLocale()));

        CommonFieldValidator validator = new CommonFieldValidator();
        if (!validator.isEmail(form.getEmail())) {
            LOG.info("Someone trying to restore wrong email = " + form.getEmail());
            errors.rejectValue("email", messageSource.getMessage("message.validate.email.correct", null, LocaleContextHolder.getLocale()));
        }

        if (!validator.isInRange(form.getEmail().length(), 1, 255)) {
            LOG.info("Someone trying to restore email not in length range 1,255 = " + form.getEmail());
            errors.rejectValue("email", messageSource.getMessage("message.validate.email.length", null, LocaleContextHolder.getLocale()));
        }
    }
}
