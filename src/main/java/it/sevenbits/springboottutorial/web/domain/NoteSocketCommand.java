package it.sevenbits.springboottutorial.web.domain;

/**
 * Created by sevenbits on 20.08.15.
 */
public class NoteSocketCommand {

    private Long id;

    private String text;

    private String emailOfShareUser;

    private String usernameOfShareUser;

    private String command;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEmailOfShareUser() {
        return emailOfShareUser;
    }

    public void setEmailOfShareUser(String emailOfShareUser) {
        this.emailOfShareUser = emailOfShareUser;
    }

    public String getUsernameOfShareUser() {
        return usernameOfShareUser;
    }

    public void setUsernameOfShareUser(String usernameOfShareUser) {
        this.usernameOfShareUser = usernameOfShareUser;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
