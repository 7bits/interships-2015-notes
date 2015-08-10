package it.sevenbits.springboottutorial.web.domain;

import javax.validation.constraints.Pattern;

public class ChangePassForm {
    private String oldPass;

    @Pattern(regexp="(([a-z]+[A-Z]+[0-9]+)|([a-z]+[0-9]+[A-Z]+)|([A-Z]+[a-z]+[0-9]+)|([A-Z]+[0-9]+[a-z]+)|([0-9]+[A-Z]+[a-z]+)|([0-9]+[a-z]+[A-Z]+))",
            message = "Пароль должен содержать хотя бы одну цифру, одну заглавную букву, одну букву в нижнем регистре.")
    private String newPass;

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
