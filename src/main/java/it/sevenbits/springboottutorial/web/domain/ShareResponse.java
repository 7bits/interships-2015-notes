package it.sevenbits.springboottutorial.web.domain;


import java.io.Serializable;

public class ShareResponse implements Serializable {
    private Boolean success;
    private String message;

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

    public ShareResponse(Boolean success, String message) {

        this.success = success;
        this.message = message;
    }
}
