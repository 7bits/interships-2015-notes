package it.sevenbits.telenote.web.controllers;

import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import it.sevenbits.telenote.web.domain.ChangesForm;
import it.sevenbits.telenote.web.service.AccountService;
import it.sevenbits.telenote.web.service.NoteService;
import it.sevenbits.telenote.web.service.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class AccountController {

    private static Logger LOG = Logger.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private NoteService noteService;

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public ModelAndView getAccount(Authentication auth) throws ServiceException {
        UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();

        ModelAndView model = new ModelAndView("home/account");
        model.addObject("user", user);
        model.addObject("changesForm", new ChangesForm("", ""));
        model.addObject("avatar", "http://www.gravatar.com/avatar/" + accountService.getAvatarHash(user.getEmail()) +
                "?d=http%3A%2F%2Ftele-notes.7bits.it%2Fresources%2Fpublic%2Fimg%2FshareNotRegUser.png");

        return model;
    }

    @RequestMapping(value = "/account", method = RequestMethod.POST)
    public @ResponseBody
    ModelAndView changePass(@Valid @ModelAttribute("form") ChangesForm form, Authentication auth) throws ServiceException {
        UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
        String username = user.getName();
        ModelAndView model = new ModelAndView("home/account");
        model.addObject("user", user);
        model.addObject("avatar", "http://www.gravatar.com/avatar/" + accountService.getAvatarHash(user.getEmail()) +
                "?d=http%3A%2F%2Ftele-notes.7bits.it%2Fresources%2Fpublic%2Fimg%2FshareNotRegUser.png");

        try {
            if (!form.getUsername().isEmpty() && !form.getUsername().equals(user.getName())) {
                user.setName(form.getUsername());
                accountService.changeUsername(user);
            }

            if (!form.getTheme().equals(user.getStyle())) {
                user.setStyle(form.getTheme());
                accountService.changeTheme(user);
            }

            if (!(form.getNewPass().isEmpty() && form.getCurrentPass().isEmpty())) {
                accountService.changePass(form.getCurrentPass(), form.getNewPass(), user);
                form.setCurrentPass("");
                form.setNewPass("");
            }
        } catch (ServiceException e) {
            if (e.getMessage().equals("incorrectPass") || e.getMessage().equals("patternFail") ||
                    e.getMessage().equals("curPassEqualsNewPass") || e.getMessage().equals("incorrectUsername")) {

                model.addObject(e.getMessage().toString(), 1);
                model.addObject("changesForm", form);

                user.setName(username);
                model.addObject("user", user);

                return model;
            } else throw new ServiceException(e.getMessage());
        }

        model.addObject("user", user);
        model.addObject("changesForm", form);
        return model;
    }
}
