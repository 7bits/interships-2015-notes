package it.sevenbits.telenote.utils.validators;

import it.sevenbits.telenote.web.domain.forms.ChangesForm;
import it.sevenbits.telenote.web.domain.forms.RestorePasswordForm;
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
public class ChangesFormValidator implements Validator {
    private static final Logger LOG = Logger.getLogger(UserCreateFormValidator.class);

    @Autowired
    private CommonFieldValidator validator;

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(ChangesForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ChangesForm form = (ChangesForm) target;
        LOG.debug("Validating user with username = " + form.getUsername());

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", messageSource.getMessage("message.validate.username.notempty", null, LocaleContextHolder.getLocale()));

        if (!validator.isValidUsername(form.getUsername())) {
            LOG.info("Someone trying to input not valid username" + form.getUsername());
            errors.rejectValue("username", messageSource.getMessage("message.validate.username.correct", null, LocaleContextHolder.getLocale()));
        }

        if (!validator.isInRange(form.getUsername().length(), 1, 255)) {
            LOG.info("Someone trying to input username not in length range 1,255 = " + form.getUsername());
            errors.rejectValue("username", messageSource.getMessage("message.validate.username.length", null, LocaleContextHolder.getLocale()));
        }

        if (!validator.isEqual(form.getCurrentPass(), form.getNewPass())) {
            if(!validator.isNotNullOrEmpty(form.getCurrentPass())) {
                LOG.info("Someone trying to input empty curPass and not empty newPass. newPass: " + form.getNewPass());
                errors.rejectValue("currentPass", messageSource.getMessage("message.validate.password.notempty", null, LocaleContextHolder.getLocale()));
            }

            if(!validator.isNotNullOrEmpty(form.getNewPass())) {
                LOG.info("Someone trying to input empty newPass and not empty curPass. curPass: " + form.getCurrentPass());
                errors.rejectValue("newPass", messageSource.getMessage("message.validate.password.notempty", null, LocaleContextHolder.getLocale()));
            }

            if (!validator.isInRange(form.getCurrentPass().length(), 5, 255)) {
                LOG.info("Someone trying to input current password not in length range 5,255 = " + form.getCurrentPass());
                errors.rejectValue("currentPass", messageSource.getMessage("message.validate.password.length", null, LocaleContextHolder.getLocale()));
            }

            if (!validator.isInRange(form.getNewPass().length(), 5, 255)) {
                LOG.info("Someone trying to input new password not in length range 5,255 = " + form.getCurrentPass());
                errors.rejectValue("newPass", messageSource.getMessage("message.validate.password.length", null, LocaleContextHolder.getLocale()));
            }
        }

    }
}
