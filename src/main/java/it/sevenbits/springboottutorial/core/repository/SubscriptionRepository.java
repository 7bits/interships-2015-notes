package it.sevenbits.springboottutorial.core.repository;

import it.sevenbits.springboottutorial.core.domain.Subscription;

import java.util.List;

public interface SubscriptionRepository {
    void save(final Subscription subscription) throws RepositoryException;
    List<Subscription> findAll() throws RepositoryException;
}
