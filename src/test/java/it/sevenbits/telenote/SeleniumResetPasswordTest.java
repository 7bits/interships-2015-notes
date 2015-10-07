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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//import static org.hamcrest.Matchers.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.URL;
import java.net.MalformedURLException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class SeleniumResetPasswordTest {
    public static final String USERNAME = "julianovikova";
    public static final String ACCESS_KEY = "685e7716-f83b-4806-949b-5d5087614606";
    public static final String URL = "http://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:80/wd/hub";

    private static WebDriver driver;

    private static UserDetailsImpl user;

    @Autowired
    @Qualifier(value = "theUserPersistRepository")
    public IUserRepository repository;

    @Autowired
    public UserService userService;

    private WebElement email;
    private WebElement username;
    private WebElement password;
    private WebElement submit;

    /*@BeforeClass
    public static void initDriver() {
        //driver = new ChromeDriver();
        driver = new FirefoxDriver();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        user = new UserDetailsImpl();
        user.setUsername("ololo@ololo.com");
        user.setName("Capitan");
        //driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
    }*/

    /*@AfterClass
    public static void closeDriver() {
        driver.quit();
    }*/

    /*@Before
    public void before() throws Exception {
        try {
            user.setPassword((new BCryptPasswordEncoder()).encode("Ololo73"));

            repository.create(user);

            user.setPassword("Ololo73");
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        driver.get("http://notes:bestpassword@tele-notes.7bits.it");

    }*/

//resetting password with valid email
    @Test
    public void resetPasswordTest() {

        DesiredCapabilities caps = DesiredCapabilities.chrome();
        caps.setCapability("platform", "Linux");
        caps.setCapability("version", "44.0");
        WebDriver driver = null;
        try {
            driver = new RemoteWebDriver(new URL(URL), caps);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (driver != null) {
            driver.get("https://notes:bestpassword@tele-notes.7bits.it/");
        }

        user = new UserDetailsImpl();
        user.setUsername("ololo@ololo.com");
        user.setName("Capitan");

        try {
            user.setPassword((new BCryptPasswordEncoder()).encode("Ololo73"));

            repository.create(user);

            user.setPassword("Ololo73");
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        driver.findElement(By.className("welcomeForm__href_color")).click();
        assertEquals("https://notes:bestpassword@tele-notes.7bits.it/resetpass", driver.getCurrentUrl());
        email = driver.findElement(By.id("js-logText"));
        email.sendKeys("ololo@ololo.com");
        submit = driver.findElement(By.className("welcomeForm__button"));
        submit.submit();

        driver.quit();

        try {
            userService.cleanDB();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

//trying to reset password with invalid email
    @Test
    public void resetInvalidEmailPasswordTest() {

        DesiredCapabilities caps = DesiredCapabilities.chrome();
        caps.setCapability("platform", "Linux");
        caps.setCapability("version", "44.0");
        WebDriver driver = null;
        try {
            driver = new RemoteWebDriver(new URL(URL), caps);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (driver != null) {
            driver.get("https://notes:bestpassword@tele-notes.7bits.it/");
        }

        user = new UserDetailsImpl();
        user.setUsername("ololo@ololo.com");
        user.setName("Capitan");

        try {
            user.setPassword((new BCryptPasswordEncoder()).encode("Ololo73"));

            repository.create(user);

            user.setPassword("Ololo73");
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        driver.findElement(By.className("welcomeForm__href_color")).click();
        assertEquals("https://notes:bestpassword@tele-notes.7bits.it/resetpass", driver.getCurrentUrl());
        email = driver.findElement(By.id("js-logText"));
        email.sendKeys("Qwerty");
        submit = driver.findElement(By.className("welcomeForm__button"));
        submit.submit();

        driver.quit();

        try {
            userService.cleanDB();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

//trying to reset password with empty field
    @Test
    public void resetEmptyEmailPasswordTest() {
        DesiredCapabilities caps = DesiredCapabilities.chrome();
        caps.setCapability("platform", "Linux");
        caps.setCapability("version", "44.0");
        WebDriver driver = null;
        try {
            driver = new RemoteWebDriver(new URL(URL), caps);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (driver != null) {
            driver.get("https://notes:bestpassword@tele-notes.7bits.it/");
        }

        user = new UserDetailsImpl();
        user.setUsername("ololo@ololo.com");
        user.setName("Capitan");

        try {
            user.setPassword((new BCryptPasswordEncoder()).encode("Ololo73"));

            repository.create(user);

            user.setPassword("Ololo73");
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        driver.findElement(By.className("welcomeForm__href_color")).click();
        assertEquals("https://notes:bestpassword@tele-notes.7bits.it/resetpass", driver.getCurrentUrl());
        email = driver.findElement(By.id("js-logText"));
        email.sendKeys(" ");
        submit = driver.findElement(By.className("welcomeForm__button"));
        submit.submit();

        driver.quit();

        try {
            userService.cleanDB();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    /*@After
    public void after() throws Exception {
    try {
        userService.cleanDB();
    } catch (Exception ex) {
        fail(ex.getMessage());
   }*/
}
