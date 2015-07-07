package it.sevenbits.springboottutorial.core.domain;

import java.io.Serializable;

public class Subscription implements Serializable {
    private Long id;
    private String email;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Subscription[id=%d, name=%s, email=%s]",
            id, name, email);
    }
}
