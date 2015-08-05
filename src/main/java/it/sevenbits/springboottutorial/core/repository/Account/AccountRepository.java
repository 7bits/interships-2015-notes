package it.sevenbits.springboottutorial.core.repository.Account;

import it.sevenbits.springboottutorial.core.mappers.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier(value = "accountRepository")
public class AccountRepository implements IAccountRepository {

    @Autowired
    private AccountMapper accountMapper;
}
