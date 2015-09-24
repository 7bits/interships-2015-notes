package it.sevenbits.telenote.web.controllers;

import it.sevenbits.telenote.utils.HttpMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Error controller.
 */
@org.springframework.web.bind.annotation.RestController
public class RestController implements ErrorController {

    @Autowired
    private HttpMessage messageSource;

    /** Default Spring error URL. */
    private static final String PATH = "/error";

    public static final String DEFAULT_ERROR_VIEW = "home/error";

    /**
     * Gets error page when input URL is not found.
     * @param request request that contains error status code.
     * @return error page with certain text.
     */
    @RequestMapping(value = PATH)
    public ModelAndView error(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView(DEFAULT_ERROR_VIEW);

        final Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

        String message = messageSource.getHttpMessage(statusCode);

        mav.addObject("errorCode", statusCode);
        mav.addObject("message", message);
        return mav;
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
