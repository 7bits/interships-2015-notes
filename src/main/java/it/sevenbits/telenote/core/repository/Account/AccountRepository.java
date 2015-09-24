package it.sevenbits.telenote.core.repository.Account;

import it.sevenbits.telenote.core.domain.UserDetailsImpl;
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
     * @param user POJO that contains theme name and user id.
     * @throws RepositoryException
     */
    @Override
    public void changeStyle(UserDetailsImpl user) throws RepositoryException {
        try {
            accountMapper.changeStyle(user);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while changing user's theme style: " + e.getMessage(), e);
        }
    }

    /**
     * Updates username by user id.
     * @param user POJO that contains username and user id.
     * @throws RepositoryException
     */
    @Override
    public void changeUsername(UserDetailsImpl user) throws RepositoryException {
        try {
            accountMapper.changeUsername(user);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while changing username: " + e.getMessage(), e);
        }
    }

    /**
     * Updates user password by user id.
     * @param user POJO that contains user password and user id.
     * @throws RepositoryException
     */
    @Override
    public void changePass(UserDetailsImpl user) throws RepositoryException {
        try {
            accountMapper.changePass(user);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while changing password: " + e.getMessage(), e);
        }
    }

    /**
     * Gets user theme by user id.
     * @param userId user id.
     * @return user theme.
     * @throws RepositoryException
     */
    @Override
    public String getUserStyle(Long userId) throws RepositoryException {
        try {
            return accountMapper.getUserStyle(userId);
        } catch (Exception e) {
            throw new RepositoryException("An error occurred while getting user's theme style: " + e.getMessage(), e);
        }
    }
}
