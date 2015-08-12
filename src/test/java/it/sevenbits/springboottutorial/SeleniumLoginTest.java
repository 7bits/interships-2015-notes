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

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class SeleniumLoginTest {

    private static WebDriver driver;

    private static UserDetailsImpl user;

    @Autowired
    @Qualifier(value = "theUserPersistRepository")
    public IUserRepository repository;

    private WebElement email;
    private WebElement password;
    private WebElement submit;

    @BeforeClass
    public static void initDriver() {
        //driver = new ChromeDriver();
        driver = new FirefoxDriver();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        user = new UserDetailsImpl();
        user.setEmail("ololo@ololo.com");
        user.setUsername("Capitan");
        //driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void closeDriver() {
        driver.close();
    }

    @Before
    public void before() throws Exception {
        try {
            user.setPassword((new BCryptPasswordEncoder()).encode("Ololo73"));

            repository.create(user);

            user.setPassword("Ololo73");
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        driver.get("http://127.0.0.1:9000");

        email = driver.findElement(By.ByCssSelector.cssSelector("form[name=signinForm] input[name=username]"));
        password = driver.findElement(By.ByCssSelector.cssSelector("form[name=signinForm] input[name=password]"));
        submit = driver.findElement(By.ByCssSelector.cssSelector("form[name=signinForm] .loginSubmit"));
    }

    @Test
    public void loginAllOk() {
        email.sendKeys(user.getEmail());
        password.sendKeys(user.getPassword());

        submit.submit();

        assertEquals("http://127.0.0.1:9000/telenote", driver.getCurrentUrl());

        WebElement element = driver.findElement(By.ByCssSelector.cssSelector(".logMenu a[href='/logout']"));
        element.click();

        assertEquals("http://127.0.0.1:9000/", driver.getCurrentUrl());
    }

    @Test
    public void loginWrongEmail() {
        email.sendKeys("ololo");
        password.sendKeys(user.getPassword());

        submit.submit();

        driver.findElement(By.className("errorDiv"));
    }

    @Test
    public void loginWrongPassword() {
        email.sendKeys(user.getEmail());
        password.sendKeys("123");

        submit.submit();

        driver.findElement(By.className("errorDiv"));
    }

    @After
    public void after() throws Exception {
        try {
            repository.remove(user);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
}