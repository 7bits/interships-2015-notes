package it.sevenbits.telenote.web.domain.models;

import it.sevenbits.telenote.core.domain.UserDetailsImpl;

/**
 * Created by vik on 06.10.15.
 */
public class UserPresentModel {
    private Long id;
    private String name;
    private String username;
    private String avatar;
    private Boolean success;
    private int code;
    private String message;

    public UserPresentModel() {}

    public UserPresentModel(UserDetailsImpl user) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
    }

    public UserPresentModel(Boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
