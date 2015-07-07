package it.sevenbits.springboottutorial.core.repository;

import it.sevenbits.springboottutorial.core.domain.Subscription;
import it.sevenbits.springboottutorial.core.mappers.SubscriptionMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Qualifier(value = "subscriptionPersistRepository")
public class SubscriptionPersistRepository implements SubscriptionRepository {
    private static Logger LOG = Logger.getLogger(SubscriptionPersistRepository.class);

    @Autowired
    private SubscriptionMapper mapper;


    @Override
    public void save(final Subscription subscription) throws RepositoryException {
        if (subscription == null) {
            throw new RepositoryException("Subscription is null");
        }
        try {
            mapper.save(subscription);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while saving subscription: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Subscription> findAll() throws RepositoryException {
        try {
            return mapper.findAll();
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while retrieving subscriptions: " + e.getMessage(), e);
        }
    }
}
