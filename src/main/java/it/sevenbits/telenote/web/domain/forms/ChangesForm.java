package it.sevenbits.telenote.web.domain.forms;

public class ChangesForm {

    private String username;

    private String currentPass;

    private String newPass;

    private String style;

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

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
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
