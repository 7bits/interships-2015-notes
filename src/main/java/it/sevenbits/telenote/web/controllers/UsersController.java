package it.sevenbits.telenote.web.controllers;

import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.template.JadeTemplate;
import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import it.sevenbits.telenote.utils.validators.ResetPasswordFormValidator;
import it.sevenbits.telenote.utils.Helper;
import it.sevenbits.telenote.utils.UtilsException;
import it.sevenbits.telenote.utils.validators.RestorePasswordFormValidator;
import it.sevenbits.telenote.web.domain.forms.ResetPassForm;
import it.sevenbits.telenote.web.domain.forms.RestorePasswordForm;
import it.sevenbits.telenote.web.domain.forms.UserCreateForm;

import it.sevenbits.telenote.service.EmailService;
import it.sevenbits.telenote.service.ServiceException;
import it.sevenbits.telenote.utils.validators.UserCreateFormValidator;
import it.sevenbits.telenote.service.UserService;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
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
import java.io.IOException;
import java.util.*;

/**
 * Created by sevenbits on 16.07.15.
 */

@Controller
public class UsersController {
    private static Logger LOG = Logger.getLogger(HomeController.class);

    @Value("${spring.domain}")
    private String domain;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCreateFormValidator userCreateFormValidator;

    @Autowired
    private RestorePasswordFormValidator restoreValidator;

    @Autowired
    private ResetPasswordFormValidator resetValidator;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JadeConfiguration jade;

    /** Validates form. */
    @InitBinder("form")
    public void initBinderSignupForm(WebDataBinder binder) {
        binder.addValidators(userCreateFormValidator);
    }

    @InitBinder("restoreForm")
    public void initBinderRestoreForm(WebDataBinder binder) {
        binder.addValidators(restoreValidator);
    }

    @InitBinder("resetForm")
    public void initBinderResetForm(WebDataBinder binder) {
        binder.addValidators(resetValidator);
    }

    /**
     * Redirects to welcome page.
     * @return welcome page.
     * @throws ServiceException
     */
    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public ModelAndView handleLoginGet() throws ServiceException {
        return new ModelAndView("redirect:/");
    }

    /**
     * Redirects to root with form in session.
     * @param session contains user form.
     * @return sign up page with user form.
     * @throws ServiceException
     */
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView handleRegistrationGet(HttpSession session) throws ServiceException {
        ModelAndView model = new ModelAndView("redirect:/");
        model.addObject("form", (UserCreateForm)session.getAttribute("userForm"));

        return model;
    }

    /**
     * Signs up when form data is valid. Gets sign up page with certain errors when form data is invalid.
     * @param form contains registration data.
     * @param bindingResult contains errors, if form is invalid.
     * @param session persists form data to show invalid fields.
     * @return error/success sign up page.
     */
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ModelAndView handleRegistrationPost(@Valid @ModelAttribute("form") UserCreateForm form,
                BindingResult bindingResult, HttpSession session) {
        try {
            if (bindingResult.hasErrors()) {
                String url = "home/welcome";
                ModelAndView model = new ModelAndView(url);
                Map<String, String> map = Helper.getSignUpErrors(bindingResult);

                for (Map.Entry<String, String> entry : map.entrySet()) {
                    model.addObject(entry.getValue() + "Error", entry.getKey());
                }

                session.setAttribute("userForm", form);
                model.addObject("signupForm", form);
                return model;
            }

/*<<<<<<< HEAD*/
            UserDetailsImpl user = new UserDetailsImpl();
            user.setName(form.getUsername());
            user.setUsername(form.getEmail().toLowerCase());
            user.setPassword(form.getPassword());

            userService.create(user);
            String link = domain + "/confirm?token=" + userService.getToken(form.getEmail().toLowerCase())
                    + "&email=" + form.getEmail().toLowerCase();
            JadeTemplate template = jade.getTemplate("mails/confirmRegMail");
            HashMap<String, Object> model = new HashMap<String, Object>();
            model.put("confirmLink", link);
            model.put("username", form.getUsername());
            String html = jade.renderTemplate(template, model);

            emailService.sendMail(form.getEmail(), messageSource.getMessage("message.confirm.email", null, LocaleContextHolder.getLocale()), html);
        } catch(UtilsException e) {
            LOG.warn(e.getMessage());
        } catch (ServiceException e) {
            LOG.info(e.getMessage());

            List<String> errors = new ArrayList<>();
            errors.add(messageSource.getMessage("message.signup.exist", null, LocaleContextHolder.getLocale()));

            ModelAndView model = new ModelAndView("home/welcome");
            session.setAttribute("userForm", form);

            return new ModelAndView("home/welcome", "errorMessages", errors);
        } catch (IOException ex) {
            LOG.warn("Cant load jade file of mail. " + ex.getMessage());
            return new ModelAndView("home/error", "error", messageSource.getMessage("message.error.500", null, LocaleContextHolder.getLocale()));
        }

        ModelAndView model = new ModelAndView("home/checkMail");
        model.addObject("message", messageSource.getMessage("message.signup.email.notice", null, LocaleContextHolder.getLocale()));
        model.addObject("title", messageSource.getMessage("message.signup.email.title", null, LocaleContextHolder.getLocale()));
        model.addObject("email", form.getEmail());

        return model;
    }

    /**
     * Sends an email to user with link to reset password.
     * @param token user token
     * @param email user email.
     * @return error/success page.
     */
    @RequestMapping(value = "/resetpass", method = RequestMethod.GET)
    public ModelAndView resetPass(String token, String email) {
        boolean isEmptyInput = token == null || email == null || token.isEmpty() || email.isEmpty();
        if (isEmptyInput) {
            ModelAndView model = new ModelAndView("home/resetPass");
            model.addObject("resetForm", new ResetPassForm());

            return model;
        }

        try {
            if (token.equals(userService.getToken(email))) {
                ModelAndView model = new ModelAndView("home/newpass", "token", token);
                model.addObject("restoreForm", new RestorePasswordForm());
                model.addObject("email", email);

                return model;
            } else {
                return new ModelAndView("home/errors", "error", messageSource.getMessage("message.resetpass.tokenerror", null, LocaleContextHolder.getLocale()));
            }
        } catch (ServiceException e) {
            return new ModelAndView("home/errors", "error", e.getMessage());
        }
    }

    @RequestMapping(value = "/resetpass", method = RequestMethod.POST)
    public ModelAndView resetPassHandler(@Valid @ModelAttribute("resetForm") ResetPassForm form, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return new ModelAndView("home/resetPass", "error", bindingResult.getFieldError("email").getCode());
            }

            String email = form.getEmail().toLowerCase();
            String token = userService.resetPassInDB(email);
            String link = domain + "/resetpass?token=" + token + "&email=" + email;

            HashMap<String, Object> map = new HashMap<>();
            map.put("link", link);
            map.put("username", email);

            JadeTemplate template = jade.getTemplate("mails/changePassMail");
            String html = jade.renderTemplate(template, map);

            emailService.sendMail(form.getEmail(), messageSource.getMessage("message.resetpass.email.title", null, LocaleContextHolder.getLocale()), html);
        } catch (ServiceException e) {
            return new ModelAndView("home/errors", "error", e.getMessage());
        } catch (IOException ex) {
            return new ModelAndView("home/errors", "error", ex.getMessage());
        }

        ModelAndView model = new ModelAndView("home/checkMail");
        model.addObject("message", messageSource.getMessage("message.resetpass.email.notice", null, LocaleContextHolder.getLocale()));
        model.addObject("title", messageSource.getMessage("message.resetpass.email.title", null, LocaleContextHolder.getLocale()));
        model.addObject("email", form.getEmail());

        return model;
    }

    @RequestMapping(value = "/updatepass", method = RequestMethod.POST)
    public ModelAndView updatePass(@Valid @ModelAttribute("restoreForm") RestorePasswordForm form) {
        try {
            String[] passwords = new String[2];
            passwords[0] = form.getPassword();
            passwords[1] = form.getPasswordRepeat();

            boolean checkError = userService.updatePass(form.getEmail(), form.getToken(), passwords);
            if (!checkError)
                return new ModelAndView("home/newpass");
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
            model.addObject("message", messageSource.getMessage("message.confirm.wrongdata", null, LocaleContextHolder.getLocale()));
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
            model.addObject("message", messageSource.getMessage("message.confirm.wrongdata", null, LocaleContextHolder.getLocale()));
            return model;
        }

        return new ModelAndView("redirect:/");
    }

    /**
     * Controller for updating typesorting of user
     * @param request
     * @param auth
     * @return redirecting string
     */
    @RequestMapping(value = "/telenote/sorting", method = RequestMethod.POST)
    public ModelAndView noteSorting(HttpServletRequest request, Authentication auth) throws NumberFormatException {
        UserDetailsImpl currentUser = (UserDetailsImpl)auth.getPrincipal();
        int type = Integer.parseInt(request.getParameter("type"));

        try {
            userService.updatingTypesorting(currentUser.getId(), type);
            LOG.info("Successful updating typesorting of user: " + currentUser.getName());
            currentUser.setTypesorting(type);
        } catch (ServiceException e) {
            LOG.info("Error updating typesorting of user: " + currentUser.getName());
        }

        return new ModelAndView("/telenote");
    }
}
