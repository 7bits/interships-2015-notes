package it.sevenbits.telenote.web.controllers;
import it.sevenbits.telenote.utils.HttpMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlerController {

    @Autowired
    private HttpMessage messageSource;

    public static final String DEFAULT_ERROR_VIEW = "home/error";

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e) {
        ModelAndView mav = new ModelAndView(DEFAULT_ERROR_VIEW);

        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

        // handling spring exception
        if(e.getMessage().equals("Request method 'GET' not supported"))
            statusCode = 404;

        String message = messageSource.getHttpMessage(statusCode);

        mav.addObject("message", message);
        mav.addObject("errorCode", statusCode);

        return mav;
    }
}