package it.sevenbits.telenote;

import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import it.sevenbits.telenote.core.repository.User.IUserRepository;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.*;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
@FlywayTest
public class UserRepositoryTests {

    @Autowired
    @Qualifier(value = "theUserPersistRepository")
    public IUserRepository repository;

    private UserDetailsImpl user = new UserDetailsImpl();

    @Before
    public void create() throws Exception {
        user.setEmail("ok@ok.oke");
        user.setPassword("qwerty");
        user.setName("Leo");

        repository.create(user);

        assertNotNull(user.getId());
        assertTrue(user.getId().longValue() > 0);
    }

    @After
    public void remove() throws Exception {
        repository.remove(user);
    }

    @Test
    public void isEmailExistsTest() throws Exception {
        assertTrue(repository.isEmailExists(user));

        user.setEmail("ololo@ololo.ololo");

        assertFalse(repository.isEmailExists(user));
    }

    @Test
    public void getPasswordByIdTest() throws Exception {
        String password = repository.getPasswordById(user);

        assertNotNull(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    public void updatePasswordTest() throws Exception {
        String pass = user.getPassword();
        user.setPassword("newpass");

        repository.updatePassword(user);

        assertEquals(repository.getPasswordById(user), user.getPassword());

        user.setPassword(pass);
        repository.updatePassword(user);

        assertEquals(repository.getPasswordById(user), user.getPassword());
    }

    public void checkUserFields(UserDetailsImpl tUser) {
        assertEquals(tUser.getId(), user.getId());
        assertEquals(tUser.getEmail(), user.getEmail());
        assertEquals(tUser.getName(), user.getName());
        assertEquals(tUser.getPassword(), user.getPassword());

        assertNotNull(tUser.getRole());
        assertNotNull(tUser.getCreatedAt());
        assertNotNull(tUser.getUpdatedAt());
        assertTrue(tUser.isEnabled());
    }

    @Test
    public void getUserByIdTest() throws Exception {
        Optional<UserDetailsImpl> tUser = repository.getUserById(user.getId());

        assertTrue(tUser.isPresent());
        checkUserFields(tUser.get());
    }

    @Test
    public void getUserByEmailTest() throws Exception {
        Optional<UserDetailsImpl> tUser = repository.getUserByEmail(user.getEmail());

        assertTrue(tUser.isPresent());
        checkUserFields(tUser.get());
    }

    @Test
    public void confirmTest() throws Exception {
        repository.confirm(user.getEmail());

        Optional<UserDetailsImpl> tUser = repository.getUserByEmail(user.getEmail());
        assertTrue(tUser.isPresent());
        assertTrue(tUser.get().getIsConfirmed());
    }

    @Test
    public void getTokenTest() throws Exception {
        String token = repository.getTokenByEmail(user.getEmail());

        assertFalse(token.isEmpty());
        assertTrue(token.length() == 32);
    }

    @Test
    public void getUserByNameTest() throws Exception {
        Optional<UserDetailsImpl> tUser = repository.getUserById(user.getId());

        assertTrue(tUser.isPresent());
        checkUserFields(tUser.get());
    }

    @Test
    public void setTokenTest() throws Exception {
        String oldToken = repository.getTokenByEmail(user.getEmail());
        String newToken =  "newtokentocomparewith";

        repository.setTokenByEmail(user.getEmail(), newToken);
        newToken = repository.getTokenByEmail(user.getEmail());

        assertNotEquals(oldToken, newToken);

        repository.setTokenByEmail(user.getEmail(), oldToken);
    }
}