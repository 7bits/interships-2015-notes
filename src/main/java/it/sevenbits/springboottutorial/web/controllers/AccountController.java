package it.sevenbits.springboottutorial.web.controllers;

import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.web.domain.ChangesForm;
import it.sevenbits.springboottutorial.web.domain.ResponseMessage;
import it.sevenbits.springboottutorial.web.service.AccountService;
import it.sevenbits.springboottutorial.web.service.NoteService;
import it.sevenbits.springboottutorial.web.service.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
        ModelAndView model = new ModelAndView("home/account");
        model.addObject("user", (UserDetailsImpl) auth.getPrincipal());
        model.addObject("changesForm", new ChangesForm("", ""));

        return model;
    }

    @RequestMapping(value = "/account", method = RequestMethod.POST)
    public @ResponseBody
    ModelAndView changePass(@Valid @ModelAttribute("form") ChangesForm form, Authentication auth) throws ServiceException {
        UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
        ModelAndView model = new ModelAndView("home/account");
        model.addObject("user", user);

        try {
            if (!form.getUsername().equals(user.getUsername())) {
                user.setUsername(form.getUsername());
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
            if (e.getMessage().equals("password") || e.getMessage().equals("oldPass") || e.getMessage().equals("username")) {
                if (e.getMessage().equals("password")) model.addObject("password", 1);
                if (e.getMessage().equals("oldPass")) model.addObject("oldPass", 1);

                model.addObject("changesForm", form);

                return model;
            } else throw new ServiceException(e.getMessage());
        }

        model.addObject("changesForm", form);
        return model;
    }
}
