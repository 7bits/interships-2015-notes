package it.sevenbits.telenote;

import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import it.sevenbits.telenote.core.repository.User.IUserRepository;
import it.sevenbits.telenote.service.UserService;
import org.junit.*;
import org.junit.runner.RunWith;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.WebDriver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class SeleniumRegistrationTest {

    private static WebDriver driver;

    @Autowired
    @Qualifier(value = "theUserPersistRepository")
    public IUserRepository repository;

    @Autowired
    public UserService userService;

    private static UserDetailsImpl user;

    private WebElement email;
    private WebElement username;
    private WebElement password;
    //private WebElement passwordRepeat;
    private WebElement submit;

    private void findInputFields() {
        email = driver.findElement(By.id("js-regEmail"));
        username = driver.findElement(By.id("js-regUsername"));
        password = driver.findElement(By.id("js-regPass"));
        submit = driver.findElement(By.className("js-regSubmit"));
    }

    @Before
    public void before() throws Exception {
        driver.get("http://127.0.0.1:9000");
        findInputFields();

        //register user
        email.sendKeys(user.getUsername());
        username.sendKeys(user.getName());
        password.sendKeys(user.getPassword());
        //passwordRepeat.sendKeys(user.getPassword());

        submit.submit();

        assertEquals("http://127.0.0.1:9000/signup", driver.getCurrentUrl());
        driver.findElement(By.className("js-backToMain")).click();

        /*List<WebElement> error = driver.findElements(By.className("errorText"));
        assertTrue(error.isEmpty());*/

        findInputFields();
    }

    @After
    public void after() throws Exception {
        //submit.submit();
        //submit.click();

        //assertEquals(driver.getCurrentUrl(), "http://127.0.0.1:9000/signup");
        //List<WebElement> error = driver.findElements(By.className("errorDiv"));
        //assertFalse(error.isEmpty());

        try {
            userService.cleanDB();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

//register new user
    @Test
    public void regAllValidTest() throws Exception {
        //findInputFields()
        email.sendKeys(user.getUsername());
        username.sendKeys(user.getName());
        password.sendKeys(user.getPassword());
        submit.submit();
    }

//register user with invalid email
    @Test
    public void regInvalidEmailTest() throws Exception {
        //WebElement email = driver.findElement(By.name("email"));
        //findInputFields();
        email.sendKeys("ololo");
        username.click();
        username.sendKeys(user.getName());
        password.sendKeys(user.getPassword());
        submit.submit();

        driver.findElement(By.className("js-emailError"));
    }

//register almost existing user
    @Test
    public void regUserExistsTest() throws Exception {
        //findInputFields();
        email.sendKeys("ololo@ololo.com");
        username.sendKeys(user.getName());
        password.sendKeys(user.getPassword());
        submit.submit();
    }

//register user with wrong username
    @Test
    public void regWrongUsernameTest() throws Exception {
        findInputFields();
        email.sendKeys("ololo1@ololo.com");
        username.sendKeys("");
        password.sendKeys(user.getPassword());
        //passwordRepeat.sendKeys(user.getPassword());
        submit.submit();

	driver.findElement(By.className("js-nameError"));
    }

//register user with username made of numbers
    @Test
    public void regNumberUsernameTest() throws Exception {
        findInputFields();
        email.sendKeys("ololo2@ololo.com");
        username.sendKeys("1234");
        password.sendKeys(user.getPassword());
        submit.submit();
    }

//register user with empty password
    @Test
    public void regEmptyPasswordTest() throws Exception {
        findInputFields();
        email.sendKeys("ololo3@ololo.com");
        username.sendKeys("Leo");
        password.sendKeys("");
        submit.submit();

        driver.findElement(By.className("js-passError"));
    }

//register user with password made of spaces
   @Test
    public void regSpacesPasswordTest() throws Exception {
        findInputFields();
        email.sendKeys("ololo4@ololo.com");
        username.sendKeys("Leo");
        password.sendKeys("        ");
        submit.submit();

        driver.findElement(By.className("js-passError"));
    }

//register user with short password
    @Test
    public void regShortPasswordTest() throws Exception {
        findInputFields();
        email.sendKeys("ololo5@ololo.com");
        username.sendKeys("Leo");
        password.sendKeys("1");
        submit.submit();

        driver.findElement(By.className("js-passError"));
    }

//register user with invalid password
    @Test
    public void regWrongPasswordTest() throws Exception {
        findInputFields();
        email.sendKeys("ololo6@ololo.com");
        username.sendKeys("Leo");
        password.sendKeys("123");
        submit.submit();

        driver.findElement(By.className("js-passError"));
    }

    @BeforeClass
    public static void initDriver() {
        //driver = new ChromeDriver();
        driver = new FirefoxDriver();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        user = new UserDetailsImpl();
        user.setUsername("ololo@ololo.com");
        user.setName("Capitan");
        user.setPassword("Ololo73");
        //driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void closeDriver() {
        driver.close();
    }
}
