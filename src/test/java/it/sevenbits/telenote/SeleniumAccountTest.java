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
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.interactions.Actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//import static org.hamcrest.Matchers.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
//@ContextConfiguration(initializers = TestContextInitializer.class)
public class SeleniumAccountTest {

    private static WebDriver driver;

    private static UserDetailsImpl user;

    @Autowired
    @Qualifier(value = "theUserPersistRepository")
    public IUserRepository repository;

    @Autowired
    public UserService userService;


    @BeforeClass
    public static void initDriver() {
        driver = new FirefoxDriver();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        user = new UserDetailsImpl();
        user.setUsername("ololo@ololo.com");
        user.setName("Capitan");
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
    public void validUserNameTest() {
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
    }
}
