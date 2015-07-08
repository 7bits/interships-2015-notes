package it.sevenbits.springboottutorial.core.auth;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Authorisation {

    private static final int MIN_LENGTH = 5;

    private static final Pattern pattern = Pattern.compile(
            "(?=^.{" +MIN_LENGTH + ",}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Za-z]).*$");


    public Authorisation() {}

    private String encryptPass(String plainPassword){

        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    private boolean isPasswordsMatch(String plainpassword, String encryptedPassword) {
        return (isPasswordValid(plainpassword))
                ? BCrypt.checkpw(plainpassword, encryptedPassword) : false;
    }

    private boolean isPasswordValid(String plainPassword) {

        Matcher matcher = pattern.matcher(plainPassword);

        if ((plainPassword == null) ||
                (plainPassword.isEmpty()) ||
                (plainPassword.length() < MIN_LENGTH) ||
                (!matcher.matches())) return false;

        return true;
    }
}

