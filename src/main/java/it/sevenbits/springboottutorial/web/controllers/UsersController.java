package it.sevenbits.springboottutorial.web.controllers;

import it.sevenbits.springboottutorial.web.domain.UserCreateForm;
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

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
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

    @InitBinder("form")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ModelAndView handleRegistration(@Valid @ModelAttribute("form") UserCreateForm form,
                BindingResult bindingResult) throws ServiceException {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());

            //TODO заполнить модель ошибками и передать в шаблон
            ModelAndView model = new ModelAndView("home/welcome");
            model.addObject("signupForm", form);
            model.addObject("errorMessages", errors);

            return model;
        }

        try {
            userService.create(form);
        } catch (ServiceException e) {
            bindingResult.reject("Create.error", "Cant create user." + e.getMessage());
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
}
