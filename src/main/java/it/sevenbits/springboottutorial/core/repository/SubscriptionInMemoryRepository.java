package it.sevenbits.springboottutorial.core.repository;

import it.sevenbits.springboottutorial.core.domain.Subscription;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Qualifier(value = "subscriptionInMemoryRepository")
public class SubscriptionInMemoryRepository implements SubscriptionRepository {
    private final static Logger LOG = Logger.getLogger(SubscriptionInMemoryRepository.class);

    private final Map<Long, Subscription> subscriptions;
    private final AtomicLong keySequence;

    public SubscriptionInMemoryRepository() {
        subscriptions = new HashMap<>();
        keySequence = new AtomicLong(1L);
    }

    @Override
    public void save(final Subscription subscription) throws RepositoryException {
        if (subscription == null) {
            LOG.error("Subscription is null");
            throw new RepositoryException("Subscription is null");
        }
        LOG.info("Start saving: " + subscription.toString());
        subscription.setId(keySequence.getAndIncrement());
        subscriptions.put(subscription.getId(), subscription);
        LOG.info("Saved: " + subscription.toString());
    }

    @Override
    public List<Subscription> findAll() {
        return new ArrayList<>(subscriptions.values());
    }
}
