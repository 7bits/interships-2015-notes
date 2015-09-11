package it.sevenbits.telenote.web.controllers;

import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import it.sevenbits.telenote.web.domain.forms.RestorePasswordForm;
import it.sevenbits.telenote.web.domain.forms.UserCreateForm;
import it.sevenbits.telenote.service.EmailService;
import it.sevenbits.telenote.service.ServiceException;
import it.sevenbits.telenote.service.validators.UserCreateFormValidator;
import it.sevenbits.telenote.service.UserService;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
        ModelAndView model = new ModelAndView("redirect:/");
        model.addObject("form", (UserCreateForm)session.getAttribute("userForm"));

        return model;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ModelAndView handleRegistrationPost(@Valid @ModelAttribute("form") UserCreateForm form,
                BindingResult bindingResult, HttpSession session, HttpServletRequest request) {
        try {
            if (bindingResult.hasErrors()) {
                String url = "home/welcome";
                ModelAndView model = userService.getSignUpErrors(url, bindingResult);

                session.setAttribute("userForm", form);
                model.addObject("signupForm", form);
                return model;
            }

            if (!form.getEmail().isEmpty()) {
                UserDetailsImpl user = new UserDetailsImpl();
                user.setName(form.getUsername());
                user.setUsername(form.getEmail().toLowerCase());
                user.setPassword(form.getPassword());

                userService.create(user);

                String link = "http://tele-notes.7bits.it/confirm?token=" + userService.getToken(form.getEmail()) + "&email=" + form.getEmail();
                //ModelAndView model = new ModelAndView("home/confirmRegMail");
                //model.addObject("confirmLink", );
                LOG.info("Sended email to " + form.getEmail());
                emailService.sendConfirm(form, "Tele-notes. Подтверждение регистрации.", link);
            }
            //request.login(form.getEmail(), form.getPassword());
        } catch (ServiceException e) {
            LOG.info(e.getMessage());

            List<String> errors = new ArrayList<>();
            errors.add("Не удалось зарегестрировать пользователя.");
            session.setAttribute("userForm", form);
            return new ModelAndView("home/welcome", "errorMessages", errors);
        }

        ModelAndView model = new ModelAndView("home/checkMail");
        model.addObject("message", "Письмо с инструкцией подтверждения регистрации выслано на ваш адрес");
        model.addObject("title", "Регистрация");
        model.addObject("email", form.getEmail());

        return model;
    }

    @RequestMapping(value = "/resetpass", method = RequestMethod.GET)
    public ModelAndView resetPass(String token, String email) {
        boolean isEmptyInput = token == null || email == null || token.isEmpty() || email.isEmpty();
        if (isEmptyInput) {
            ModelAndView model = new ModelAndView("home/resetPass");
            model.addObject("resetForm", new UserCreateForm());

            return model;
        }

        try {
            if (token.equals(userService.getToken(email))) {
                ModelAndView model = new ModelAndView("home/newpass", "token", token);
                model.addObject("resetForm", new RestorePasswordForm());
                model.addObject("email", email);

                return model;
            } else {
                return new ModelAndView("home/errors", "error", "Неверный или устаревший токен.");
            }
        } catch (ServiceException e) {
            return new ModelAndView("home/errors", "error", e.getMessage());
        }
    }

    @RequestMapping(value = "/resetpass", method = RequestMethod.POST)
    public ModelAndView resetPassInDB(@ModelAttribute UserCreateForm form) {
        try {
            ModelAndView model = userService.resetPassInDB(form.getEmail());
            if (model != null) return model;
        } catch (ServiceException e) {
            return new ModelAndView("home/errors", "error", e.getMessage());
        }

        ModelAndView model = new ModelAndView("home/checkMail");
        model.addObject("message", "Письмо с инструкцией подтверждения регистрации выслано на ваш адрес:");
        model.addObject("title", "Восстановление пароля");
        model.addObject("email", form.getEmail());

        return model;
    }

    @RequestMapping(value = "/updatepass", method = RequestMethod.POST)
    public ModelAndView updatePass(@ModelAttribute RestorePasswordForm form) {
        try {
            String[] passwords = new String[2];
            passwords[0] = form.getPassword();
            passwords[1] = form.getPasswordRepeat();

            ModelAndView model = userService.updatePass(form.getEmail(), form.getToken(), passwords);

            if (model != null) return model;
        } catch (ServiceException ex) {
            return new ModelAndView("home/errors", "error", ex.getMessage());
        }

        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    public ModelAndView confirmEmail(String token, String email) {
        if (token == null || email == null || token.isEmpty() || email.isEmpty()) {
            ModelAndView model = new ModelAndView("home/errors");
            model.addObject("header", "403");
            model.addObject("message", "Переданные данные не корректны, попробуйте ещё раз.");
            return model;
        }

        try {
            Optional<UserDetailsImpl> user = userService.getUserByEmail(email);
            if (!user.isPresent() && !user.get().getToken().equals(token)) {
                throw new ServiceException();
            }

            userService.confirm(email);
            LOG.info(email + " confirmed!!");
        } catch (ServiceException ex) {
            ModelAndView model = new ModelAndView("home/errors");
            model.addObject("header", "403");
            model.addObject("message", "Переданные данные не корректны, попробуйте ещё раз.");
            return model;
        }

        return new ModelAndView("redirect:/");
    }


}
