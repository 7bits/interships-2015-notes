package it.sevenbits.springboottutorial.web.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by sevenbits on 12.08.15.
 */
public class RestorePasswordForm {

    @NotNull(message="Пароль не должен быть пустым")
    @Size(min=5, max=255, message="Пароль, допустимое количество символов от 5 до 255")
    private String oldPass;

    @NotNull(message="Пароль не должен быть пустым")
    @Size(min=5, max=255, message="Пароль, допустимое количество символов от 5 до 255")
    @Pattern(regexp="(([a-z]+[A-Z]+[0-9]+)|([a-z]+[0-9]+[A-Z]+)|([A-Z]+[a-z]+[0-9]+)|([A-Z]+[0-9]+[a-z]+)|([0-9]+[A-Z]+[a-z]+)|([0-9]+[a-z]+[A-Z]+))",
            message = "Пароль должен содержать хотя бы одну цифру, одну заглавную букву, одну букву в нижнем регистре.")
    private String newPass;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }
}
