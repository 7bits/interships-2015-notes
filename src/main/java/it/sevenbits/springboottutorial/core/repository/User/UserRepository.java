package it.sevenbits.springboottutorial.core.repository.User;

//import it.sevenbits.springboottutorial.core.domain.Subscription;
import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
//import it.sevenbits.springboottutorial.core.mappers.SubscriptionMapper;
import it.sevenbits.springboottutorial.core.mappers.UserMapper;
import it.sevenbits.springboottutorial.core.repository.RepositoryException;
//import it.sevenbits.springboottutorial.core.repository.Subscription.SubscriptionRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Qualifier(value = "theUserPersistRepository")
public class UserRepository implements IUserRepository {
    private static Logger LOG = Logger.getLogger(UserRepository.class);

    @Autowired
    private UserMapper mapper;


    @Override
    public void save(final UserDetailsImpl userDetails) throws RepositoryException {
        if (userDetails == null) {
            throw new RepositoryException("Subscription is null");
        }
        try {
            mapper.save(userDetails);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while saving subscription: " + e.getMessage(), e);
        }
    }
}