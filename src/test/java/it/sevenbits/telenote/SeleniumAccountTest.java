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

import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

//import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
//@ContextConfiguration(initializers = TestContextInitializer.class)
public class SeleniumAccountTest {
    private static WebDriver driver;

    @Autowired
    @Qualifier(value = "theUserPersistRepository")
    public IUserRepository repository;

    @Autowired
    public UserService userService;
    private static UserDetailsImpl user;

    @BeforeClass
    public static void initDriver() {
        //driver = new ChromeDriver();
        driver = new FirefoxDriver();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        user = new UserDetailsImpl();
        user.setUsername("bits@ololo.com");
        user.setName("Bits");
        //driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void closeDriver() {
        driver.quit();
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

        WebDriverWait wait = new WebDriverWait(driver, 30);
        ExpectedCondition e = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return ((JavascriptExecutor)d).executeScript("return document.readyState").equals("complete");
            }
        };
        wait.until(e);

        driver.get("http://127.0.0.1:9000");

        WebElement email = driver.findElement(By.xpath("/html/body/div/div[2]/form/input[2]"));
        WebElement password = driver.findElement(By.xpath("/html/body/div/div[2]/form/input[3]"));
        WebElement submit = driver.findElement(By.xpath("/html/body/div/div[2]/form/button"));

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
        driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div/a[1]/div")).click();

        assertTrue(driver.getCurrentUrl().equals("http://127.0.0.1:9000/account"));

        WebElement toClear = driver.findElement(By.xpath("/html/body/div/form/div[1]/div[2]/input"));
        toClear.sendKeys(Keys.CONTROL + "a");
        toClear.sendKeys(Keys.DELETE);
        toClear.sendKeys("J");

        WebElement button = driver.findElement(By.xpath("/html/body/div/form/button"));
        Actions action = new Actions(driver);
        action.moveToElement(button);
        action.perform();
        button.click();
    }

//changing username from letters to symbols
    @Test
    public void symbolsUserNameTest() {
        driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div/a[1]/div")).click();

        assertTrue(driver.getCurrentUrl().equals("http://127.0.0.1:9000/account"));

        WebElement toClear = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div/a[1]/div"));
        toClear.sendKeys(Keys.CONTROL + "a");
        toClear.sendKeys(Keys.DELETE);
        toClear.sendKeys("!@#%^&*$");

       WebElement button = driver.findElement(By.xpath("/html/body/div/form/button"));
        Actions action = new Actions(driver);
        action.moveToElement(button);
        action.perform();
        button.click();
    }

//change username to spaces
    @Test
    public void emptyUserNameTest() {
        driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div/a[1]/div")).click();

        assertTrue(driver.getCurrentUrl().equals("http://127.0.0.1:9000/account"));

        WebElement toClear = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div/a[1]/div"));
        toClear.sendKeys(Keys.CONTROL + "a");
        toClear.sendKeys(Keys.DELETE);
        toClear.sendKeys("   ");

       WebElement button = driver.findElement(By.xpath("/html/body/div/form/button"));
        Actions action = new Actions(driver);
        action.moveToElement(button);
        action.perform();
        button.click();
    }

//change password in account to a new one
    @Test
    public void validUserPasswordTest() {
        driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div/a[1]/div")).click();

        assertTrue(driver.getCurrentUrl().equals("http://127.0.0.1:9000/account"));

        WebElement toClear = driver.findElement(By.xpath("/html/body/div/form/div[2]/div[2]/input"));
	    toClear.sendKeys(Keys.CONTROL + "a");
        toClear.sendKeys(Keys.DELETE);
        toClear.sendKeys("Ololo73");
	    driver.findElement(By.xpath("/html/body/div/form/div[2]/div[3]/input")).sendKeys("Capitan1234");

        WebElement button = driver.findElement(By.xpath("/html/body/div/form/button"));
        Actions action = new Actions(driver);
        action.moveToElement(button);
        action.perform();
        button.click();
    }

//change password to an empty one
    @Test
    public void emptyUserPasswordTest() {
        driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div/a[1]/div")).click();

        assertTrue(driver.getCurrentUrl().equals("http://127.0.0.1:9000/account"));

        WebElement toClear = driver.findElement(By.xpath("/html/body/div/form/div[2]/div[2]/input"));
	    toClear.sendKeys(Keys.CONTROL + "a");
        toClear.sendKeys(Keys.DELETE);
        toClear.sendKeys("Ololo73");
	    driver.findElement(By.xpath("/html/body/div/form/div[2]/div[3]/input")).sendKeys(" ");

        WebElement button = driver.findElement(By.xpath("/html/body/div/form/button"));
        Actions action = new Actions(driver);
        action.moveToElement(button);
        action.perform();
        button.click();
    }

//change password to a short one
    @Test
    public void shortUserPasswordTest() {
        driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div/a[1]/div")).click();

        assertTrue(driver.getCurrentUrl().equals("http://127.0.0.1:9000/account"));

        WebElement toClear = driver.findElement(By.xpath("/html/body/div/form/div[2]/div[2]/input"));
	    toClear.sendKeys(Keys.CONTROL + "a");
        toClear.sendKeys(Keys.DELETE);
        toClear.sendKeys("Ololo73");
	    driver.findElement(By.xpath("/html/body/div/form/div[2]/div[3]/input")).sendKeys("123");

        WebElement button = driver.findElement(By.xpath("/html/body/div/form/button"));
        Actions action = new Actions(driver);
        action.moveToElement(button);
        action.perform();
        button.click();
    }

//change account design
    @Test
    public void userDesignTest() {
        driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div/a[1]/div")).click();

        assertTrue(driver.getCurrentUrl().equals("http://127.0.0.1:9000/account"));
        WebElement button = driver.findElement(By.id("lightDen"));
        Actions action = new Actions(driver);
        action.moveToElement(button);
        action.perform();
        button.click();

	driver.findElement(By.xpath("/html/body/div/form/button")).click();
    }
}
