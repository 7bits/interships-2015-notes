package it.sevenbits.telenote.web.domain.forms;

//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

//import javax.validation.Constraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;
//import it.sevenbits.telenote.service.validators.*;
import it.sevenbits.telenote.service.validators.CommonFieldValidator;

//import org.springframework.stereotype.Component;
/*import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;*/

//@ConfigurationProperties(locations = "classpath:ValidationMessages.properties")

/**
 * Form for creating new users.
 */
public class UserCreateForm {

    @NotNull(message="Почтовый адрес не должен быть пустым")
    @Size(min=1, max=255, message="Почтовый адрес, допустимое количество символов от 1 до 255")
    @Pattern(regexp=CommonFieldValidator.VALID_EMAIL_ADDRESS_REGEX, message="e-mail не валиден")
    private String email;

    @NotNull(message="Имя пользователя не должно быть пустым.")
    @Pattern(regexp="^[A-Za-z0-9]+", message="Имя пользователя может состоять только из латинских букв и цифр.")
    @Size(min=1, max=255, message="Имя пользователя, допустимое количество символов от 1 до 255")
    private String username;

    @NotNull(message="Пароль не должен быть пустым.")
    @Size(min=5, max=255, message="Пароль, допустимое количество символов от 5 до 255")
    @Pattern(regexp="(([a-z]+[A-Z]+[0-9]+)|([a-z]+[0-9]+[A-Z]+)|([A-Z]+[a-z]+[0-9]+)|([A-Z]+[0-9]+[a-z]+)|([0-9]+[A-Z]+[a-z]+)|([0-9]+[a-z]+[A-Z]+)|([0-9]+[a-z]+)|([a-z]+[0-9]+))",
    message="Пароль должен содержать хотя бы одну цифру, одну заглавную букву, одну букву в нижнем регистре.")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserForm{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
