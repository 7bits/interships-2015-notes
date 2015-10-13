package it.sevenbits.telenote.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Helper class for different goals.
 */

public class Helper {

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Gets gravatar URL for show user avatar. Hashes user email and concat in required string.
     * @param email user email
     * @return gravatar URL for show user avatar.
     */
    public static String getAvatarUrl(String email) {
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();

        StringBuilder avatarLink = new StringBuilder();

        avatarLink.append("http://www.gravatar.com/avatar/");
        avatarLink.append(encoder.encodePassword(email, null));
        avatarLink.append("?d=http%3A%2F%2Ftele-notes.7bits.it%2Fresources%2Fpublic%2Fimg%2Fgulp%2FshareNotRegUser.png");

        return avatarLink.toString();
    }

    /**
     * Gets attributeName - attrValue map from specified request. Checks for error attribute specified in FailureHandler.
     * @param attrNames list of attribute names.
     * @param request request from user.
     * @return attributeName - attrValue map from specified request
     */
    public static Map<String, Object> getAttributeMap(List<String> attrNames, HttpServletRequest request) {
        // map attrName-attr
        Map<String, Object> map = new HashMap<>();

        // FailureHandler contains attr "error"
        Object errorAttr = request.getSession().getAttribute("error");

        if (errorAttr != null) {
            map.put("error", true);
            request.getSession().removeAttribute("error");

            for (String attrName : attrNames) {
                Object attr = request.getSession().getAttribute(attrName);

                if(attr != null) {
                    map.put(attrName, (String) attr);
                }
                request.getSession().removeAttribute(attrName);
            }
        }
        return map;
    }

    public static Map<String, String> getSignUpErrors(BindingResult bindingResult) throws UtilsException{
        Map<String, String> matcher = new HashMap<>();

        for (ObjectError objectError : bindingResult.getAllErrors()) {
            try {
                String errCod = objectError.getCode();
                if (!matcher.containsKey(errCod)) {
                    matcher.put(errCod, ((FieldError) objectError).getField().toString());
                }
            } catch (Exception e) {
                //LOG.error("Wrong type of error, expected FieldError, get another. " + e.getMessage());
                throw new UtilsException(e.getMessage());
            }
        }
        return matcher;
    }
}
