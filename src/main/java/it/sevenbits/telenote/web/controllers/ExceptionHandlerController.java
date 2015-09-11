package it.sevenbits.telenote.web.controllers;

import it.sevenbits.telenote.exceptions.ResourceNotFoundException;
import org.apache.log4j.Logger;
//import org.apache.xpath.operations.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Date;

//@Controller
//public class ExceptionHandlerController {
//
//    private static Logger LOG = Logger.getLogger(ExceptionHandlerController.class);
//
//    @RequestMapping(value = "/{path:.+}", method = RequestMethod.GET)
//    public ModelAndView checkError(@PathVariable("path") String path) throws Exception {
//        if (!path.equals("account") && !path.equals("telenote") && !path.equals("")
//                && !path.equals("resetpass") && !path.equals("confirm")) {
//            throw new ResourceNotFoundException("404", "Такой страницы не существует");
//        } else {
//            return new ModelAndView("home/"+path);
//        }
//    }
//
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ModelAndView notFound(ResourceNotFoundException e) {
//        ModelAndView model = new ModelAndView("home/errors", "header", e.getErrorCode());
//        model.addObject("message", e.getErrorMessage());
//
//        return model;
//    }
//}

@ControllerAdvice
public class ExceptionHandlerController {

    public static final String DEFAULT_ERROR_VIEW = "home/error";

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request, ResourceNotFoundException e) {
        ModelAndView mav = new ModelAndView(DEFAULT_ERROR_VIEW);

        final Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (requestUri == null) {
            requestUri = "Unknown";
        }

        // create a message to be sent back via the model object.
        final String message = MessageFormat.format("{0} returned for {1}",
                e.getErrorCode(), e.getErrorMessage());

        mav.addObject("errorCode", statusCode);
        mav.addObject("message", message);
        return mav;
    }
}