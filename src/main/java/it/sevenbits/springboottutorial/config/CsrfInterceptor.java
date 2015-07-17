package it.sevenbits.springboottutorial.config;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class CsrfInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView mav
    ) throws Exception {
        // Специальный фикс для jade, так как сама библиотека jade не складывает csrf токен в модель.
        mav.addObject("_csrf", request.getAttribute("_csrf"));
    }
}
