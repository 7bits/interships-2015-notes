package it.sevenbits.springboottutorial.web.controllers;

import it.sevenbits.springboottutorial.web.domain.UserForm;
import it.sevenbits.springboottutorial.web.service.*;

import org.apache.catalina.startup.ClassLoaderFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.ws.Service;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    private static Logger LOG = Logger.getLogger(HomeController.class);

    @Autowired
    private UserFormValidator validator;

    @Autowired
    private UserService service;

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String telenote(final Model model) {
        model.addAttribute("subscription", new UserForm());
        return "home/signin";
    }

    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public String index(final Model model) {
        model.addAttribute("subscription", new UserForm());
        return "home/signin";
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public String subscribe(@ModelAttribute UserForm form, final Model model) throws ServiceException {
        /*final Map<String, String> errors = validator.validate(form);
        if (errors.size() != 0) {
            // Если есть ошибки в форме, то снова рендерим главную страницу
            model.addAttribute("subscription", form);
            model.addAttribute("errors", errors);
            LOG.info("Subscription form contains errors.");
            return "home/errors";
        }*/

        model.addAttribute("subscription", form);
        return (service.signIn(form)) ? "home/telenote" : "home/errors";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String registration(final Model model) {
        model.addAttribute("subscription", new UserForm());
        return "home/signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String registration(@ModelAttribute UserForm form, final Model model) throws ServiceException {
        /*final Map<String, String> errors = validator.validate(form);
        if (errors.size() != 0) {
            // Если есть ошибки в форме, то снова рендерим главную страницу
            model.addAttribute("subscription", form);
            model.addAttribute("errors", errors);
            LOG.info("Subscription form contains errors.");
            return "home/errors";
        }*/

        service.save(form);
        model.addAttribute("subscription", form);
        return "home/telenote";
    }

    @RequestMapping(value = "/resetPass", method = RequestMethod.GET)
    public String resetPass(final Model model) {
        model.addAttribute("subscription", new UserForm());
        return "home/resetPass";
    }

    @RequestMapping(value = "/resetPass", method = RequestMethod.POST)
    public String resetPassInDB(@ModelAttribute UserForm form, final Model model) {

        final String password = "456";

        try {
            service.updatePass(form, password);
        } catch (Exception e) {

        }

        model.addAttribute("subscription", form);
        return "home/signin";
    }
}
