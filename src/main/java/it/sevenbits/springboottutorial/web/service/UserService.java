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
        userDetails.setPassword(form.getPassword());
        try {
            if (repository.isEmailExists(userDetails))
                throw new ServiceException("Sorry, e-mail is already exists");

            repository.save(userDetails);
        } catch (Exception e) {
            throw new ServiceException("An error occurred while saving subscription: " + e.getMessage(), e);
        }
    }

    public boolean signIn(final UserForm form) throws ServiceException {
        final UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setEmail(form.getEmail());
        userDetails.setPassword(form.getPassword());

        try {
            Long id = repository.getIdByEmail(userDetails);

            if (id < 0)
                throw new ServiceException("Incorrect username or password!");

            userDetails.setId(id);

            String password = repository.getPasswordById(userDetails);

            return userDetails.getPassword().equals(password) ? true : false;
        } catch (Exception e) {
            throw new ServiceException("An error occurred while sign in user: " + e.getMessage(), e);
        }
    }

    public void updatePass(final UserForm form, String password) throws ServiceException {

        final UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setEmail(form.getEmail());
        userDetails.setPassword(password);

        try {
            if (repository.isEmailExists(userDetails)) {
                userDetails.setId(repository.getIdByEmail(userDetails));
                repository.updatePass(userDetails);
            } else {
                throw new ServiceException("E-mail is not exists!");
            }
        }
        catch (Exception e) {
            throw new ServiceException("E-mail is not exists!");
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
