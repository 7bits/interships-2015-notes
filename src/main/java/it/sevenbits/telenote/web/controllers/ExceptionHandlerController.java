package it.sevenbits.telenote.web.controllers;
import it.sevenbits.telenote.utils.HttpMessage;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Class for showing errors if exception happened
 */
@ControllerAdvice
public class ExceptionHandlerController {

    @Autowired
    private HttpMessage messageSource;

    /** Default URL for errors */
    public static final String DEFAULT_ERROR_VIEW = "home/error";

    /**
     * Gets error page with certain text.
     * @param request request that contains error status code.
     * @param e thrown exception.
     * @return ModelAndView of error page.
     */
    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e) {
        ModelAndView mav = new ModelAndView(DEFAULT_ERROR_VIEW);

        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

        // handling spring exception
        if(e instanceof HttpRequestMethodNotSupportedException)
            statusCode = 404;

        String message = messageSource.getHttpMessage(statusCode);

        mav.addObject("message", message);
        mav.addObject("errorCode", statusCode);

        return mav;
    }
}
