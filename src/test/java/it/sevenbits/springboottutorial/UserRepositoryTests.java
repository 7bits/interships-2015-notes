package it.sevenbits.springboottutorial;

import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.repository.User.IUserRepository;
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
public class UserRepositoryTests {

    @Autowired
    @Qualifier(value = "theUserPersistRepository")
    public IUserRepository repository;

    private UserDetailsImpl user = new UserDetailsImpl();

    @Before
    public void create() throws Exception {
        user.setEmail("ok@ok.oke");
        user.setPassword("qwerty");
        user.setUsername("Leo");

        repository.create(user);

        Long id = repository.getIdByEmail(user);

        assertNotNull(id);
        assertTrue(id.longValue() > 0);

        user.setId(id);
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
        assertEquals(tUser.getUsername(), user.getUsername());
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
    public void getUserByNameTest() throws Exception {
        Optional<UserDetailsImpl> tUser = repository.getUserById(user.getId());

        assertTrue(tUser.isPresent());
        checkUserFields(tUser.get());
    }
}
