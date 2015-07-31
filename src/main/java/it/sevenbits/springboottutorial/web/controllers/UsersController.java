package it.sevenbits.springboottutorial.web.controllers;

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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import org.springframework.validation.BindingResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

    @InitBinder("form")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public ModelAndView handleLoginGet() throws ServiceException {
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView handleRegistrationGet(HttpSession session) throws ServiceException {
        ModelAndView mav = new ModelAndView("redirect:/");
        mav.addObject("form", (UserCreateForm)session.getAttribute("userForm"));

        return mav;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ModelAndView handleRegistrationPost(@Valid @ModelAttribute("form") UserCreateForm form,
                BindingResult bindingResult, HttpSession session, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());

            session.setAttribute("userForm", form);

            ModelAndView model = new ModelAndView("home/welcome");
            model.addObject("signupForm", form);
            model.addObject("errorMessages", errors);

            return model;
        }

        try {
            form.setEmail(form.getEmail().toLowerCase());

            userService.create(form);

            if (!form.getEmail().isEmpty()) {
                String link = "http://tele-notes.7bits.it/confirm?token=" + userService.getToken(form.getEmail()) + "&email=" + form.getEmail();
                //ModelAndView model = new ModelAndView("home/confirmRegMail");
                //model.addObject("confirmLink", );
                LOG.info("Sended email to " + form.getEmail());
                emailService.sendConfirm(form.getEmail(), "Tele-notes. Подтверждение регистрации.", link);
            }
            //request.login(form.getEmail(), form.getPassword());
        } catch (ServiceException e) {
            LOG.info(e.getMessage());

            List<String> errors = new ArrayList<>();
            errors.add("Не удалось зарегестрировать пользователя.");
            session.setAttribute("userForm", form);
            return new ModelAndView("home/welcome", "errorMessages", errors);
        } /*catch (ServletException e) {
            LOG.info(e.getMessage());
        }*/

        return new ModelAndView("home/checkMail");
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

    /*@RequestMapping(value = "/send", method = RequestMethod.GET)
    public String sendEmail(String email) {
        try {
            if (!email.isEmpty()) {
                String link = "http://tele-notes.7bits.it/confirm?token=" + userService.getToken(email) + "&email=" + email;
                //ModelAndView model = new ModelAndView("home/confirmRegMail");
                //model.addObject("confirmLink", );
                LOG.info("Sended email to " + email);
                emailService.sendConfirm(email, "Tele-notes. Подтверждение регистрации.", link);
            }
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage());
        }

        return "redirect:/";
    }*/

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
