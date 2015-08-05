package it.sevenbits.springboottutorial.web.service;

import it.sevenbits.springboottutorial.core.repository.Account.IAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    @Qualifier(value = "accountRepository")
    private IAccountRepository accountRepository;
}
