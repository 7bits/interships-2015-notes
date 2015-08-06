package it.sevenbits.springboottutorial.web.domain;


import java.io.Serializable;

public class ResponseMessage implements Serializable {
    private Boolean success;
    private String message;
    private String username;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ResponseMessage(Boolean success, String message) {

        this.success = success;
        this.message = message;
    }

    public ResponseMessage(Boolean success, String message, String username) {

        this.success = success;
        this.message = message;
        this.username = username;
    }
}
