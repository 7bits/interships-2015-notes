package it.sevenbits.telenote.web.domain;


import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;

import java.io.Serializable;

public class ResponseMessage implements Serializable {
    private Boolean success;
    private String message;
    private UserDetailsImpl user;

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

    public UserDetailsImpl getUser() {
        return user;
    }

    public void setUser(UserDetailsImpl user) {
        this.user = user;
    }

    public ResponseMessage(Boolean success, String message) {

        this.success = success;
        this.message = message;
    }

    public ResponseMessage(Boolean success, String message, UserDetailsImpl user) {

        this.success = success;
        this.message = message;
        this.user = user;
    }
}
