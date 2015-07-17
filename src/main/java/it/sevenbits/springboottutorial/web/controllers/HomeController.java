package it.sevenbits.springboottutorial.web.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.ModelAndView;
//import it.sevenbits.springboottutorial.web.domain.UserCreateForm;
//import it.sevenbits.springboottutorial.web.service.*;

import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
    private static Logger LOG = Logger.getLogger(HomeController.class);
    //private static Long user_id;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage() {
        return "home/welcome";
    }

    @RequestMapping(value = "/telenote", method = RequestMethod.GET)
    public ModelAndView telenote(Authentication authentication) {
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();

        return new ModelAndView("home/errors", "name", currentUser.getUsername());
    }
}
