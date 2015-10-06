package it.sevenbits.telenote.utils.validators;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CommonFieldValidator {

    /** Email exists pattern */
    public static final String VALID_EMAIL_ADDRESS_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    private static final Pattern VALID_EMAIL_ADDRESS_PATTERN = Pattern.compile(
            VALID_EMAIL_ADDRESS_REGEX, Pattern.CASE_INSENSITIVE
    );
    /** Pattern for whitespaces */
    private static final String WHITESPACE_PATTERN = "\\s+";

    /** Pattern for username */
    private static final String USERNAME_PATTERN = "[a-zA-Zа-яА-ЯёЁ0-9-_]+";

    /**
     * Validate whether value is not null and empty or contains only spaces, otherwise reject it
     *
     * @param value  Value of field
     */
    public boolean isNotNullOrEmpty(final String value) {
        return !(value == null || value.isEmpty() || value.matches(WHITESPACE_PATTERN));
    }

    public boolean isValidUsername(final String value) {
        return !(value == null || !value.matches(USERNAME_PATTERN));
    }

    /**
     * Validate whether value is valid email, otherwise reject it
     *
     * @param value  Value of field
     */
    public boolean isEmail(final String value) {
        if (value != null) {
            Matcher matcher = VALID_EMAIL_ADDRESS_PATTERN .matcher(value);
            return matcher.find();
        }

        return false;
    }

    /**
     * Validate, whether value is too long
     *
     * @param value     Value of field
     * @param maxLength Length allowed
     */
    public boolean isShorterThan(final String value, final Integer maxLength) {
        if (value != null) {
            return value.length() < maxLength;
        }

        return false;
    }

    public boolean isEqual(String value1, String value2) {
        return value1.equals(value2);
    }

    public boolean isInRange(int value, int right, int left) {
        return value >= right && value <= left;
    }
}
