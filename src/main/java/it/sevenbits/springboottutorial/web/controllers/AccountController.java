package it.sevenbits.springboottutorial.web.controllers;

import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.web.domain.ResponseMessage;
import it.sevenbits.springboottutorial.web.service.AccountService;
import it.sevenbits.springboottutorial.web.service.NoteService;
import it.sevenbits.springboottutorial.web.service.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AccountController {

    private static Logger LOG = Logger.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private NoteService noteService;

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String getAccount(final Model model, Authentication auth) throws ServiceException {
        model.addAttribute("user", (UserDetailsImpl) auth.getPrincipal());

        return "home/account";
    }

    @RequestMapping(value = "/account/style{style:\\w+}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<ResponseMessage> changeTheme(@PathVariable("style") String style, Authentication auth) throws ServiceException {
        UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
        user.setStyle(style);

        accountService.chacgeTheme(user);
        return new ResponseEntity<>(new ResponseMessage(true, "ok"), HttpStatus.OK);
    }

    @RequestMapping(value = "/account/changename/{name:.+}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<ResponseMessage> changeUsername(@PathVariable("name") String name, Authentication auth) throws ServiceException {
        UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
        user.setUsername(name);

        try {
            return accountService.changeUsername(user);
        } catch (ServiceException e) {
            return new ResponseEntity<>(new ResponseMessage(false, "Ошибка при сохранении"), HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

}
