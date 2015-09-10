package it.sevenbits.telenote.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vik on 10.09.15.
 */

public class Helper {

    public static String getAvatarUrl(String email) {
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();

        StringBuilder avatarLink = new StringBuilder();

        avatarLink.append("http://www.gravatar.com/avatar/");
        avatarLink.append(encoder.encodePassword(email, null));
        avatarLink.append("?d=http%3A%2F%2Ftele-notes.7bits.it%2Fresources%2Fpublic%2Fimg%2FshareNotRegUser.png");

        return avatarLink.toString();
    }

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
}
