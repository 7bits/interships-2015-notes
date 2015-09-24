package it.sevenbits.telenote.config;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Adds csrf token into model.
 */
@Service
public class CsrfInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView mav
    ) throws Exception {
        // Special fix for jade, because jade lib doesnt add csrf token to the model
        if (mav != null) {
            mav.addObject("_csrf", request.getAttribute("_csrf"));
        }
    }
}
