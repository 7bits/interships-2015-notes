package it.sevenbits.springboottutorial.web.service.validators;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CommonFieldValidator {

    /** Email exists pattern */
    public static final String VALID_EMAIL_ADDRESS_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    private static final Pattern VALID_EMAIL_ADDRESS_PATTERN = Pattern.compile(
            VALID_EMAIL_ADDRESS_REGEX, Pattern.CASE_INSENSITIVE
    );
    /** Pattern for whitespaces */
    private static final String WHITESPACE_PATTERN = "\\s+";

    /**
     * Validate whether value is not null and empty or contains only spaces, otherwise reject it
     *
     * @param value  Value of field
     */
    public boolean isNotNullOrEmpty(final String value) {
        return !(value == null || value.isEmpty() || value.matches(WHITESPACE_PATTERN));
    }

    /**
     * Validate whether value is valid email, otherwise reject it
     *
     * @param value  Value of field
     */
    public boolean isEmail(final String value) {
        if (value != null) {
            Matcher matcher = VALID_EMAIL_ADDRESS_PATTERN .matcher(value);
            if (!matcher.find()) {
                return false;
            }

            return true;
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
            if (value.length() > maxLength) {
                return false;
            }

            return true;
        }

        return false;
    }
}
