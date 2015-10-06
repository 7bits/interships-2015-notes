package it.sevenbits.telenote.web.domain.models;

/**
 * Created by vik on 06.10.15.
 */
public class UserPresentModel {
    private String name;
    private String username;
    private String avatar;

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
}
