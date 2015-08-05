package it.sevenbits.springboottutorial.core.repository.Account;

import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.mappers.AccountMapper;
import it.sevenbits.springboottutorial.core.repository.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier(value = "accountRepository")
public class AccountRepository implements IAccountRepository {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public void changeTheme(UserDetailsImpl user) throws RepositoryException {
        accountMapper.changeTheme(user);
    }

    @Override
    public void changeUsername(UserDetailsImpl user) throws RepositoryException {
        accountMapper.changeUsername(user);
    }

}
