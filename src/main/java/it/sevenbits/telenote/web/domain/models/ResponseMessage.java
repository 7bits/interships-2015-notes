package it.sevenbits.telenote.web.domain.models;

import it.sevenbits.telenote.core.domain.UserDetailsImpl;

import java.io.Serializable;

/**
 * Model for showing to user different success/failure messages.
 */
public class ResponseMessage implements Serializable {
    private Boolean success;
    private String message;
    private UserPresentModel user;

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

    public UserPresentModel getUser() {
        return user;
    }

    public void setUser(UserPresentModel user) {
        this.user = user;
    }

    public ResponseMessage() {};

    public ResponseMessage(Boolean success, String message) {

        this.success = success;
        this.message = message;
    }

    public ResponseMessage(Boolean success, String message, UserPresentModel user) {

        this.success = success;
        this.message = message;
        this.user = user;
    }
}
