package it.sevenbits.telenote;

import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import it.sevenbits.telenote.core.repository.User.IUserRepository;
import it.sevenbits.telenote.service.UserService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

//import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
//@ContextConfiguration(initializers = TestContextInitializer.class)
public class exampletest {
    public static final String USERNAME = "jnovikova";
    public static final String ACCESS_KEY = "800b6843-fb79-4cec-96d4-dbd5ccead76c";
    public static final String URL = "http://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:80/wd/hub";

    private static UserDetailsImpl user;

    @Autowired
    @Qualifier(value = "theUserPersistRepository")
    public IUserRepository repository;

    @Autowired
    public UserService userService;


    /*@BeforeClass
    public static void initDriver() throws MalformedURLException {
        DesiredCapabilities caps = DesiredCapabilities.firefox();
        caps.setCapability("platform", "Linux");
        caps.setCapability("version", "38.0");
        WebDriver driver = new RemoteWebDriver(new URL(URL), caps);

        // driver = new FirefoxDriver();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        user = new UserDetailsImpl();
        user.setUsername("ololo@ololo.com");
        user.setName("Capitan");
        //driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
    }*/

    //changing username in account
    @Test
    public void validUserNameTest () {

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
        driver.findElement(By.className("welcomeForm__button"));

        driver.quit();

    }
}
