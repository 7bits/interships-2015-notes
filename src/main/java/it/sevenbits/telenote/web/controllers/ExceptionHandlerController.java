package it.sevenbits.telenote.web.controllers;
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
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (requestUri == null) {
            requestUri = "Unknown";
        }

        // create a message to be sent back via the model object.
        String message = "";

        if (statusCode.equals(403)) { message = "Вы сделали что-то запрещённое для сервера"; }
        else if (statusCode.equals(405)) { message = "Данное действие сейчас не доступно"; }
        else if (statusCode.equals(500)) { message = "Ошибка на сервере, \n" +
                "когда вы вернётесь на главную, всё должно быть в норме"; }
        else { message = "Упс, что-то пошло н так, попробуйте поворить поздней"; }

        mav.addObject("message", message);
        mav.addObject("errorCode", statusCode);

        return mav;
    }
}