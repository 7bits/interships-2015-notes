package it.sevenbits.springboottutorial.web.service;

import it.sevenbits.springboottutorial.core.domain.Subscription;
import it.sevenbits.springboottutorial.core.repository.SubscriptionRepository;
import it.sevenbits.springboottutorial.web.domain.SubscriptionForm;
import it.sevenbits.springboottutorial.web.domain.SubscriptionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubscriptionsService {
    @Autowired
    @Qualifier(value = "subscriptionPersistRepository")
    private SubscriptionRepository repository;

    public void save(final SubscriptionForm form) throws ServiceException {
        final Subscription subscription = new Subscription();
        subscription.setEmail(form.getEmail());
        subscription.setName(form.getName());
        try {
            repository.save(subscription);
        } catch (Exception e) {
            throw new ServiceException("An error occurred while saving subscription: " + e.getMessage(), e);
        }
    }

    public List<SubscriptionModel> findAll() throws ServiceException {
        try {
            List<Subscription> subscriptions = repository.findAll();
            List<SubscriptionModel> models = new ArrayList<>(subscriptions.size());
            for (Subscription s: subscriptions) {
                models.add(new SubscriptionModel(
                    s.getId(),
                    s.getName(),
                    s.getEmail()
                ));
            }

            return models;
        } catch (Exception e) {
            throw new ServiceException("An error occurred while retrieving subscriptions: " + e.getMessage(), e);
        }
    }
}
