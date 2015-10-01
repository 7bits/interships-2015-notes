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
public class SeleniumAccountTest {
    public static final String USERNAME = "jnovikova";
    public static final String ACCESS_KEY = "800b6843-fb79-4cec-96d4-dbd5ccead76c";
    public static final String URL = "http://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:80/wd/hub";

    private static UserDetailsImpl user;

    @Autowired
    @Qualifier(value = "theUserPersistRepository")
    public IUserRepository repository;

    @Autowired
    public UserService userService;


    @BeforeClass
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
    }

    @AfterClass
    public static void closeDriver() throws MalformedURLException {
        DesiredCapabilities caps = DesiredCapabilities.firefox();
        caps.setCapability("platform", "Linux");
        caps.setCapability("version", "38.0");
        WebDriver driver = new RemoteWebDriver(new URL(URL), caps);
        driver.close();
    }

    @Before
    public void before() throws Exception {
        DesiredCapabilities caps = DesiredCapabilities.firefox();
        caps.setCapability("platform", "Linux");
        caps.setCapability("version", "38.0");
        WebDriver driver = new RemoteWebDriver(new URL(URL), caps);
        driver.get("http://127.0.0.1:9000");

        try {
            user.setPassword((new BCryptPasswordEncoder()).encode("Ololo73"));

            repository.create(user);

            user.setPassword("Ololo73");
        } catch (Exception ex) {
            fail(ex.getMessage());
        }


        WebDriverWait wait = new WebDriverWait(driver, 30);
        ExpectedCondition e = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return ((JavascriptExecutor)d).executeScript("return document.readyState").equals("complete");
            }
        };
        wait.until(e);

        WebElement email = driver.findElement(By.id("js-logText"));
        WebElement password = driver.findElement(By.className("js-logPass"));
        WebElement submit = driver.findElement(By.className("js-logSubmit"));

        email.sendKeys(user.getUsername());
        password.sendKeys(user.getPassword());
        submit.submit();

        assertTrue(driver.getCurrentUrl().equals("http://127.0.0.1:9000/telenote"));
    }

    @After
	public void after() throws Exception {
        try {
            userService.cleanDB();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

//changing username in account
    @Test
    public void validUserNameTest() throws MalformedURLException {
        DesiredCapabilities caps = DesiredCapabilities.firefox();
        caps.setCapability("platform", "Linux");
        caps.setCapability("version", "38.0");
        WebDriver driver = new RemoteWebDriver(new URL(URL), caps);

        driver.findElement(By.className("js-user")).click();

        assertTrue(driver.getCurrentUrl().equals("http://127.0.0.1:9000/account"));

        WebElement toClear = driver.findElement(By.id("js-username"));
        toClear.sendKeys(Keys.CONTROL + "a");
        toClear.sendKeys(Keys.DELETE);
  	    toClear.sendKeys("J");

       WebElement button = driver.findElement(By.className("js-submit"));
        Actions action = new Actions(driver);
        action.moveToElement(button);
        action.perform();
        button.click();
    }
/*
//changing username from letters to symbols
    @Test
    public void symbolsUserNameTest() {
        driver.findElement(By.className("js-user")).click();

        assertTrue(driver.getCurrentUrl().equals("http://127.0.0.1:9000/account"));

        WebElement toClear = driver.findElement(By.id("js-username"));
        toClear.sendKeys(Keys.CONTROL + "a");
        toClear.sendKeys(Keys.DELETE);
        toClear.sendKeys("!@#%^&*$");

       WebElement button = driver.findElement(By.className("js-submit"));
        Actions action = new Actions(driver);
        action.moveToElement(button);
        action.perform();
        button.click();
    }

//change username to spaces
    @Test
    public void emptyUserNameTest() {
        driver.findElement(By.className("js-user")).click();

        assertTrue(driver.getCurrentUrl().equals("http://127.0.0.1:9000/account"));

        WebElement toClear = driver.findElement(By.id("js-username"));
        toClear.sendKeys(Keys.CONTROL + "a");
        toClear.sendKeys(Keys.DELETE);
        toClear.sendKeys("   ");

       WebElement button = driver.findElement(By.className("js-submit"));
        Actions action = new Actions(driver);
        action.moveToElement(button);
        action.perform();
        button.click();
    }

//change password in account to a new one
    @Test
    public void validUserPasswordTest() {
        driver.findElement(By.className("js-user")).click();

        assertTrue(driver.getCurrentUrl().equals("http://127.0.0.1:9000/account"));

        WebElement toClear = driver.findElement(By.id("js-currentPass"));
	    toClear.sendKeys(Keys.CONTROL + "a");
        toClear.sendKeys(Keys.DELETE);
  	    toClear.sendKeys("Ololo73");
	    driver.findElement(By.id("js-newPass")).sendKeys("Capitan1234");

        WebElement button = driver.findElement(By.className("js-submit"));
        Actions action = new Actions(driver);
        action.moveToElement(button);
        action.perform();
        button.click();
    }

//change password to an empty one
    @Test
    public void emptyUserPasswordTest() {
        driver.findElement(By.className("js-user")).click();

        assertTrue(driver.getCurrentUrl().equals("http://127.0.0.1:9000/account"));

        WebElement toClear = driver.findElement(By.id("js-currentPass"));
	    toClear.sendKeys(Keys.CONTROL + "a");
        toClear.sendKeys(Keys.DELETE);
  	    toClear.sendKeys("Ololo73");
	    driver.findElement(By.id("js-newPass")).sendKeys(" ");

        WebElement button = driver.findElement(By.className("js-submit"));
        Actions action = new Actions(driver);
        action.moveToElement(button);
        action.perform();
        button.click();
    }

//change password to a short one
    @Test
    public void shortUserPasswordTest() {
        driver.findElement(By.className("js-user")).click();

        assertTrue(driver.getCurrentUrl().equals("http://127.0.0.1:9000/account"));

        WebElement toClear = driver.findElement(By.id("js-currentPass"));
	    toClear.sendKeys(Keys.CONTROL + "a");
        toClear.sendKeys(Keys.DELETE);
  	    toClear.sendKeys("Ololo73");
	    driver.findElement(By.id("js-newPass")).sendKeys("123");

        WebElement button = driver.findElement(By.className("js-submit"));
        Actions action = new Actions(driver);
        action.moveToElement(button);
        action.perform();
        button.click();
    }

//change account design
    @Test
    public void userDesignTest() {
        driver.findElement(By.className("js-user")).click();

        assertTrue(driver.getCurrentUrl().equals("http://127.0.0.1:9000/account"));

  	WebElement button = driver.findElement(By.id("lightDen"));
        Actions action = new Actions(driver);
        action.moveToElement(button);
        action.perform();
        button.click();

	driver.findElement(By.className("js-submit")).click();
    }*/
}
