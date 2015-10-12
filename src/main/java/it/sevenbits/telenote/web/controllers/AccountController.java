package it.sevenbits.telenote.web.controllers;

import it.sevenbits.telenote.core.domain.UserDetailsImpl;

import it.sevenbits.telenote.utils.Helper;
import it.sevenbits.telenote.utils.UtilsException;
import it.sevenbits.telenote.utils.validators.ChangesFormValidator;
import it.sevenbits.telenote.web.domain.forms.ChangesForm;
import it.sevenbits.telenote.service.AccountService;
import it.sevenbits.telenote.service.NoteService;
import it.sevenbits.telenote.service.ServiceException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AccountController {

    private static Logger LOG = Logger.getLogger(AccountController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AccountService accountService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private ChangesFormValidator changesFormValidator;

    /** Validates form. */
    @InitBinder("form")
    public void initBinderSignupForm(WebDataBinder binder) {
        binder.addValidators(changesFormValidator);
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public ModelAndView getAccount(Authentication auth) throws ServiceException {
        UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();

        ModelAndView model = new ModelAndView("home/account");
        model.addObject("user", user);
        model.addObject("changesForm", new ChangesForm("", ""));
        model.addObject("avatar", Helper.getAvatarUrl(user.getUsername()));

        return model;
    }

    /**
     * Validates form and changes account settings if it's correct.
     * @param form transmitted ChangesForm.
     * @param auth for user identification.
     * @return ModelAndView with
     * @throws ServiceException
     */
    @RequestMapping(value = "/account", method = RequestMethod.POST)
    public @ResponseBody
    ModelAndView changeAccountSettings(@Valid @ModelAttribute("form") ChangesForm form, BindingResult bindingResult,
                                       Authentication auth) {
        UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
        String username = user.getName();
        ModelAndView model = new ModelAndView("home/account");
        model.addObject("user", user);
        model.addObject("avatar", Helper.getAvatarUrl(user.getUsername()));

        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> map = Helper.getSignUpErrors(bindingResult);

                for (Map.Entry<String, String> entry : map.entrySet()) {
                    model.addObject(entry.getValue() + "Error", entry.getKey());
                }
                return model;
            }

            Map<String, Object> settingsMap = accountService.changeAccountSettings(form, user);
            model.addAllObjects(settingsMap);
        } catch (UtilsException e) {
            LOG.warn(e.getMessage());
            return new ModelAndView("home/error", "error", messageSource.getMessage("message.error.500", null, LocaleContextHolder.getLocale()));
        } catch (ServiceException e) {
            LOG.warn(e.getMessage());
            return new ModelAndView("home/error", "error", messageSource.getMessage("message.error.500", null, LocaleContextHolder.getLocale()));
        }

        form.setCurrentPass("");
        form.setNewPass("");
        model.addObject("user", user);
        model.addObject("changesForm", form);
        return model;
    }
}
