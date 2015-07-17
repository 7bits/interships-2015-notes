package it.sevenbits.springboottutorial.web.controllers;

import it.sevenbits.springboottutorial.web.domain.UserLoginForm;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by sevenbits on 16.07.15.
 */
@Controller
public class LoginController {
    private static Logger LOG = Logger.getLogger(HomeController.class);

    /*@Autowired
    private UserService userService;*/

    /*@Autowired
    private NoteService noteService;*/

    /*@InitBinder("form")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(new UserCreateFormValidator());
    }*/

    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public ModelAndView login(String error) {
        ModelAndView model = new ModelAndView("home/signin", "form", new UserLoginForm());
        if (error == null) {
            return model;
        }
        if (error.equals("true")) {
            model.addObject("error", true);
        }

        return model;
    }

    /*@RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ModelAndView handleLogin(@Valid @ModelAttribute("form") UserLoginForm form,
                              BindingResult bindingResult) throws ServiceException {

        if (!bindingResult.hasErrors()) {
            try {
                userService.signIn(form);
                return new ModelAndView("home/telenote", "notes", );
            } catch (ServiceException e) {
                bindingResult.reject("Create.error", "Cant create user." + e.getMessage());
            }
        }

        return "home/errors";
    }*/

}
