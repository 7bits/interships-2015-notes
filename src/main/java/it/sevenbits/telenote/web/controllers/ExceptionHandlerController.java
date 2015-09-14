package it.sevenbits.telenote.web.controllers;
import it.sevenbits.telenote.utils.HttpMessage;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlerController {

    public static final String DEFAULT_ERROR_VIEW = "home/error";

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e) {
        ModelAndView mav = new ModelAndView(DEFAULT_ERROR_VIEW);

        final Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

        //String message = HttpMessage.getHttpMessage(statusCode);
        String message = e.getMessage();

        mav.addObject("message", message);
        mav.addObject("errorCode", statusCode);

        return mav;
    }
}