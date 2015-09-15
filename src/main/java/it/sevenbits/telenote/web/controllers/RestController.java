package it.sevenbits.telenote.web.controllers;

import it.sevenbits.telenote.utils.HttpMessage;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@org.springframework.web.bind.annotation.RestController
public class RestController implements ErrorController {

    private static final String PATH = "/error";
    public static final String DEFAULT_ERROR_VIEW = "home/error";

    @RequestMapping(value = PATH)
    public ModelAndView error(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView(DEFAULT_ERROR_VIEW);

        final Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

        String message = HttpMessage.getHttpMessage(statusCode);

        mav.addObject("errorCode", statusCode);
        mav.addObject("message", message);
        return mav;
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
