package it.sevenbits.telenote.web.domain.forms;

/**
 * Form for creating new users.
 */
public class UserCreateForm {

    private String email;

    private String username;

    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserForm{" +
        "email='" + email + '\'' +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        '}';
    }
}
