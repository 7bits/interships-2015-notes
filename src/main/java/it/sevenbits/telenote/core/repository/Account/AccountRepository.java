package it.sevenbits.telenote.core.repository.Account;

import it.sevenbits.telenote.core.mappers.AccountMapper;
import it.sevenbits.telenote.core.repository.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier(value = "accountRepository")
public class AccountRepository implements IAccountRepository {

    @Autowired
    private AccountMapper accountMapper;

    /**
     * Updates user theme by user id.
     * @param userId user id.
     */
    @Override
    public void changeStyle(Long userId) throws RepositoryException {
        try {
            accountMapper.changeStyle(userId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while changing user's theme style: " + e.getMessage(), e);
        }
    }

    @Override
    public void changeUsername(Long userId) throws RepositoryException {
        try {
            accountMapper.changeUsername(userId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while changing username: " + e.getMessage(), e);
        }
    }

    @Override
    public void changePass(Long userId) throws RepositoryException {
        try {
            accountMapper.changePass(userId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while changing password: " + e.getMessage(), e);
        }
    }

    @Override
    public String getUserStyle(Long userId) throws RepositoryException {
        try {
            return accountMapper.getUserStyle(userId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting user's theme style: " + e.getMessage(), e);
        }
    }
}
