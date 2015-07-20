package it.sevenbits.springboottutorial;

//import it.sevenbits.springboottutorial.core.repository.User.IUserRepository;
//import it.sevenbits.springboottutorial.core.repository.User.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class UserRepositoryTests {

    @Test
    public void simpleTest() throws Exception {
        System.out.println("Hello World!");
        assertTrue("Everything is fine?", true);
    }
}
