package it.sevenbits.springboottutorial.web.controllers;

import it.sevenbits.springboottutorial.exceptions.ResourceNotFoundException;
import org.apache.log4j.Logger;
//import org.apache.xpath.operations.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ExceptionHandlerController {

    private static Logger LOG = Logger.getLogger(ExceptionHandlerController.class);

    @RequestMapping(value = "/{path:.+}", method = RequestMethod.GET)
    public ModelAndView checkError(@PathVariable("path") String path) throws Exception {
        if (!path.equals("account") && !path.equals("telenote") && !path.equals("")
                && !path.equals("resetpass") && !path.equals("confirm")) {
            throw new ResourceNotFoundException("404", "Такой страницы не существует");
        } else {
            return new ModelAndView("home/"+path);
        }
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView notFound(ResourceNotFoundException e) {
        ModelAndView model = new ModelAndView("home/errors", "header", e.getErrorCode());
        model.addObject("message", e.getErrorMessage());

        return model;
    }
}
