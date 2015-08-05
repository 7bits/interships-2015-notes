package it.sevenbits.springboottutorial.web.controllers;

import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.web.service.AccountService;
import it.sevenbits.springboottutorial.web.service.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AccountController {

    private static Logger LOG = Logger.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String getAccount(final Model model, Authentication auth) throws ServiceException {
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();
        model.addAttribute("username", currentUser.getUsername());
        return "home/account";
    }

}
