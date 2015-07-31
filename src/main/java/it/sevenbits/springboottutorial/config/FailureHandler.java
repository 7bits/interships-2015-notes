package it.sevenbits.springboottutorial.config;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.*;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class FailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception)
                                        throws IOException, ServletException {

        request.getSession().setAttribute("ERROR", true);
        request.getSession().setAttribute("USER_NAME", exception.getAuthentication().getPrincipal().toString());
        //request.getSession().getAttribute("email");
        super.onAuthenticationFailure(request, response, exception);
    }
}
