package it.sevenbits.telenote.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles fail auth.
 */
public class FailureHandler extends SimpleUrlAuthenticationFailureHandler {
    /**
     * Place error and username attributes into user session.
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception)
                                        throws IOException, ServletException {

        request.getSession().setAttribute("error", true);
        request.getSession().setAttribute("username", exception.getAuthentication().getPrincipal().toString());
        //request.getSession().getAttribute("email");
        super.onAuthenticationFailure(request, response, exception);
    }
}
