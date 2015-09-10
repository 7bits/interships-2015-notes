package it.sevenbits.telenote.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

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
}
