package it.sevenbits.springboottutorial.web.controllers;

import de.neuland.jade4j.spring.template.SpringTemplateLoader;
import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.web.domain.UserCreateForm;
import it.sevenbits.springboottutorial.web.service.EmailService;
import it.sevenbits.springboottutorial.web.service.ServiceException;
import it.sevenbits.springboottutorial.web.service.validators.UserCreateFormValidator;
import it.sevenbits.springboottutorial.web.service.UserService;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.View;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


//import java.util.NoSuchElementException;
/**
 * Created by sevenbits on 16.07.15.
 */
@Controller
public class UsersController {
    private static Logger LOG = Logger.getLogger(HomeController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserCreateFormValidator validator;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SpringTemplateLoader jade;

    @InitBinder("form")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public ModelAndView handleLoginGet() throws ServiceException {
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView handleRegistrationGet() throws ServiceException {
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ModelAndView handleRegistrationPost(@Valid @ModelAttribute("form") UserCreateForm form,
                BindingResult bindingResult) throws ServiceException {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());

            ModelAndView model = new ModelAndView("home/welcome");
            model.addObject("signupForm", form);
            model.addObject("errorMessages", errors);

            return model;
        }

        try {
            form.setEmail(form.getEmail().toLowerCase());

            userService.create(form);
        } catch (ServiceException e) {
            LOG.info(e.getMessage());

            List<String> errors = new ArrayList<>();
            errors.add("Не удалось зарегестрировать пользователя.");

            return new ModelAndView("home/welcome", "errorMessages", errors);
        }

        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/resetPass", method = RequestMethod.GET)
    public String resetPass(final Model model) {
        /*model.addAttribute("subscription", new UserCreateForm());
        return "home/resetPass";*/
        return "home/errors";
    }

    @RequestMapping(value = "/resetPass", method = RequestMethod.POST)
    public String resetPassInDB(@ModelAttribute UserCreateForm form, final Model model) {

        /*final String password = "456";

        try {
            userService.updatePassword(form, password);
        } catch (Exception e) {

        }

        model.addAttribute("subscription", form);
        return "home/signin";*/
        return "home/errors";
    }

    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public String sendEmail(String email) {
        try {
            if (!email.isEmpty()) {
                String link = "http://tele-notes.7bits.it/confirm?token=" + userService.getToken(email) + "&email=" + email;
                //ModelAndView model = new ModelAndView("home/confirmRegMail");
                //model.addObject("confirmLink", );

                emailService.sendConfirm(email, "Tele-notes. Подтверждение регистрации.", link);
            }
        } catch (ServiceException e) {

        }

        return "redirect:/";
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    public String confirmEmail(String token, String email) {
        if (token.isEmpty() || email.isEmpty()) {
            return "home/errors";
        }

        try {
            Optional<UserDetailsImpl> user = userService.getUserByEmail(email);
            if (!user.isPresent() && !user.get().getToken().equals(token)) {
                return "home/errors";
            }

            userService.confirm(email);
            LOG.info(email + " confirmed!!");
        } catch (ServiceException ex) {
            return "home/errors";
        }

        return "redirect:/";
    }
}
