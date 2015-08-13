package it.sevenbits.springboottutorial.web.domain;

import javax.validation.constraints.Pattern;

public class ChangesForm {

    private String username;

    private String currentPass;

    private String newPass;

    private String theme;

    public ChangesForm(String currentPass, String newPass) {
        this.currentPass = currentPass;
        this.newPass = newPass;
    }

    public ChangesForm() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getCurrentPass() {
        return currentPass;
    }

    public void setCurrentPass(String currentPass) {
        this.currentPass = currentPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }
}
