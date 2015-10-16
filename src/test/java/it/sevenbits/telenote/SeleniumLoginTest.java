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
public class SeleniumLoginTest {

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
    private WebElement submit;

    private void findInputFields() {
        email = driver.findElement(By.xpath("/html/body/div/div[3]/form/input[2]"));
        username = driver.findElement(By.xpath("/html/body/div/div[3]/form/input[3]"));
        password = driver.findElement(By.xpath("/html/body/div/div[3]/form/input[4]"));
        submit = driver.findElement(By.xpath("/html/body/div/div[3]/form/button"));
    }

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

        driver.get("http://127.0.0.1:9000");

        email = driver.findElement(By.xpath("/html/body/div/div[2]/form/input[2]"));
        password = driver.findElement(By.xpath("/html/body/div/div[2]/form/input[3]"));
        submit = driver.findElement(By.xpath("/html/body/div/div[2]/form/button"));
    }

    @After
    public void after() throws Exception {
        try {
            userService.cleanDB();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
   }

    @Test
    public void loginAllOk() {
        email.sendKeys(user.getUsername());
        password.sendKeys(user.getPassword());
        submit.submit();

        assertEquals("http://127.0.0.1:9000/telenote", driver.getCurrentUrl());

        WebElement element = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div/a[2]"));
        element.click();

        assertEquals("http://127.0.0.1:9000/", driver.getCurrentUrl());
    }


//trying to log in with empty input fields
    @Test
    public void loginEmptyInputsTest() {
        email.sendKeys("");
        password.sendKeys("");

        submit.submit();

        driver.findElement(By.id("js-loginError"));
    }

//trying to log in with invalid email
    @Test
    public void loginWrongEmail() {
        email.sendKeys("bits");
        password.sendKeys(user.getPassword());

        submit.submit();

        driver.findElement(By.id("js-loginError"));
    }

//trying to log in with wrong password
    @Test
    public void loginWrongPasswordTest() {
        email.sendKeys(user.getUsername());
        password.sendKeys("123");

        submit.submit();

        driver.findElement(By.id("js-loginError"));
    }

//trying to log in without confirmation email
    @Test
    public void loginWithoutConfirmationTest() {
        findInputFields();
        email.sendKeys("bits1@ololo.com");
        username.sendKeys("Bits");
        password.sendKeys("Capitan1234");
        submit.submit();

        assertEquals("http://127.0.0.1:9000/signup", driver.getCurrentUrl());
        driver.findElement(By.className("js-backToMain")).click();

        email = driver.findElement(By.xpath("/html/body/div/div[2]/form/input[2]"));
        password = driver.findElement(By.xpath("/html/body/div/div[2]/form/input[3]"));
        submit = driver.findElement(By.xpath("/html/body/div/div[2]/form/button"));
        email.sendKeys("bits1@ololo.com");
        password.sendKeys("Capitan1234");
        submit.submit();
    }
}
