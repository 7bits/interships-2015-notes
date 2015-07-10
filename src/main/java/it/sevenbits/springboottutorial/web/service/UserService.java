package it.sevenbits.springboottutorial.web.service;

//import it.sevenbits.springboottutorial.core.domain.Subscription;
import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.repository.User.IUserRepository;
import it.sevenbits.springboottutorial.web.domain.UserForm;
//import it.sevenbits.springboottutorial.web.domain.SubscriptionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    @Qualifier(value = "theUserPersistRepository")
    private IUserRepository repository;

    public void save(final UserForm form) throws ServiceException {
        final UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setEmail(form.getEmail());
        userDetails.setUsername(form.getUsername());
        try {
            repository.save(userDetails);
        } catch (Exception e) {
            throw new ServiceException("An error occurred while saving subscription: " + e.getMessage(), e);
        }
    }

    /*public List<SubscriptionModel> findAll() throws ServiceException {
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
    }*/
}
