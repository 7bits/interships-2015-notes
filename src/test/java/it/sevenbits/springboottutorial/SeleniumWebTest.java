package it.sevenbits.springboottutorial;

import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.repository.User.IUserRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//import static org.hamcrest.Matchers.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class SeleniumWebTest {

    private static WebDriver driver;

    @Autowired
    @Qualifier(value = "theUserPersistRepository")
    public IUserRepository repository;

    private static UserDetailsImpl user;

    private WebElement email;
    private WebElement username;
    private WebElement password;
    private WebElement passwordRepeat;
    private WebElement submit;

    @Before
    public void before() throws Exception {
        try {
            user = new UserDetailsImpl();
            user.setEmail("ololo@ololo.com");
            user.setPassword((new BCryptPasswordEncoder()).encode("Ololo73"));
            user.setUsername("Capitan");

            repository.create(user);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        user.setPassword("Ololo73");

        driver.get("http://127.0.0.1:9000");
        WebElement element = driver.findElement(By.className("js-reg"));

        assertTrue(element.isEnabled());
        assertTrue(element.isDisplayed());

        element.click();

        email = driver.findElement(By.ByCssSelector.cssSelector("form[name=signupForm] input[name=email]"));
        username = driver.findElement(By.ByCssSelector.cssSelector("form[name=signupForm] input[name=username]"));
        password = driver.findElement(By.ByCssSelector.cssSelector("form[name=signupForm] input[name=password]"));
        passwordRepeat = driver.findElement(By.ByCssSelector.cssSelector("form[name=signupForm] input[name=passwordRepeat]"));
        submit = driver.findElement(By.ByCssSelector.cssSelector("form[name=signupForm] .regSubmit"));

        /*assertTrue(email.isEnabled());
        assertTrue(email.isDisplayed());
        assertTrue(username.isEnabled());
        assertTrue(username.isDisplayed());
        assertTrue(password.isEnabled());
        assertTrue(password.isDisplayed());
        assertTrue(passwordRepeat.isEnabled());
        assertTrue(passwordRepeat.isDisplayed());*/
    }

    @After
    public void after() throws Exception {
        submit.submit();
        //submit.click();

        List<WebElement> error = driver.findElements(By.className("errorText"));
        assertFalse(error.isEmpty());

        try {
            repository.remove(user);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void regWrongEmailTest() throws Exception {
        //WebElement email = driver.findElement(By.name("email"));
        email.sendKeys("ololo");
        username.click();
        username.sendKeys(user.getUsername());
        password.sendKeys(user.getPassword());
        passwordRepeat.sendKeys(user.getPassword());
    }

    @Test
    public void regWrongUsernameTest() throws Exception {
        email.sendKeys(user.getEmail());
        username.sendKeys("Leo");
        password.sendKeys(user.getPassword());
        passwordRepeat.sendKeys(user.getPassword());
    }

    @Test
    public void regWrongPasswordTest() throws Exception {
        email.sendKeys(user.getEmail());
        username.sendKeys(user.getUsername());
        password.sendKeys("123");
        passwordRepeat.sendKeys(user.getPassword());
    }

    @Test
    public void regWrongPasswordRepeatTest() throws Exception {
        email.sendKeys(user.getEmail());
        username.sendKeys(user.getUsername());
        password.sendKeys(user.getPassword());
        passwordRepeat.sendKeys("123");
    }

    @Test
    public void regUserExist() throws Exception {
        email.sendKeys(user.getEmail());
        username.sendKeys(user.getUsername());
        password.sendKeys(user.getPassword());
        passwordRepeat.sendKeys(user.getPassword());
    }

    /*@Test
    public void reg*/

    @BeforeClass
    public static void initDriver() {
        //driver = new ChromeDriver();
        driver = new FirefoxDriver();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        //driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void closeDriver() {
        driver.close();
    }
}
