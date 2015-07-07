package it.sevenbits.springboottutorial.web.domain;

public class SubscriptionModel {
    private final Long id;
    private final String email;
    private final String name;

    public SubscriptionModel(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("Subscription[id=%d, name=%s, email=%s]",
            id, name, email);
    }
}
