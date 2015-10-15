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

    private void findInputFields() {
        email = driver.findElement(By.cssSelector("html body.body div.container div.container__welcomeForm form.welcomeForm__form input#js-regEmail.welcomeForm__textbox"));
        username = driver.findElement(By.xpath("/html/body/div/div[3]/form/input[3]"));
        password = driver.findElement(By.xpath("/html/body/div/div[3]/form/input[4]"));
        submit = driver.findElement(By.xpath("/html/body/div/div[3]/form/button"));
    }

    /*@BeforeClass
    public static void initDriver() {
        //driver = new ChromeDriver();
        driver = new FirefoxDriver();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);*/

        /*user = new UserDetailsImpl();
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

        driver.get("http://127.0.0.1:9000");

        email = driver.findElement(By.id("js-logText"));
        password = driver.findElement(By.className("js-logPass"));
        submit = driver.findElement(By.className("js-logSubmit"));
    }*/

//test for logging in with valid data
    @Test
    public void loginAllOk() {
        DesiredCapabilities caps = DesiredCapabilities.chrome();
        caps.setCapability("platform", "Linux");
        caps.setCapability("version", "44.0");
        WebDriver driver = null;
        try {
            driver = new RemoteWebDriver(new URL(URL), caps);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        user = new UserDetailsImpl();
        user.setUsername("ololo@ololo.com");
        user.setName("Capitan");

        /*try {
            user.setPassword((new BCryptPasswordEncoder()).encode("Ololo73"));

            repository.create(user);

            user.setPassword("Ololo73");
        } catch (Exception ex) {
            fail(ex.getMessage());
        }*/

        if (driver != null) {
            driver.get("https://notes:bestpassword@tele-notes.7bits.it/");
        }

        /*email = driver.findElement(By.xpath("/html/body/div/div[2]/form/input[2]"));
        //username = driver.findElement(By.xpath("/html/body/div/div[2]/form/input[3]"));
        password = driver.findElement(By.cssSelector("html body.body div.container div.container__welcomeForm form.welcomeForm__form input.welcomeForm__textbox"));
        submit = driver.findElement(By.xpath("/html/body/div/div[2]/form/button")).click();*/

        findInputFields();

        email.sendKeys(user.getUsername());
        password.sendKeys(user.getPassword());
        submit.submit();

        /*driver.findElement(By.xpath("/html/body/div/div[2]/form/input[2]")).sendKeys("ololo@ololo.com");
        driver.findElement(By.xpath("/html/body/div/div[2]/form/input[3]")).sendKeys("Ololo73");
        driver.findElement(By.xpath("/html/body/div/div[2]/form/button")).click();

        assertEquals("https://notes:bestpassword@tele-notes.7bits.it/telenote", driver.getCurrentUrl());*/

        /*WebElement element = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div/a[2]"));
        element.click();

        assertEquals("https://notes:bestpassword@tele-notes.7bits.it/", driver.getCurrentUrl());*/

        try {
            userService.cleanDB();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        driver.quit();
    }

//trying to log in with empty input fields
    /*@Test
    public void loginEmptyInputsTest() {
        email.sendKeys("");
        password.sendKeys("");

        submit.submit();

        driver.findElement(By.id("js-loginError"));
    }

//trying to log in with invalid email
    @Test
    public void loginWrongEmail() {
        email.sendKeys("ololo");
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
        email.sendKeys("ololo1@ololo.com");
        username.sendKeys("Capitan");
        password.sendKeys("Capitan1234");
        submit.submit();

        assertEquals("http://127.0.0.1:9000/signup", driver.getCurrentUrl());
        driver.findElement(By.className("js-backToMain")).click();

        email = driver.findElement(By.id("js-logText"));
        password = driver.findElement(By.className("js-logPass"));
        submit = driver.findElement(By.className("js-logSubmit"));
        email.sendKeys("ololo1@ololo.com");
        password.sendKeys("Capitan1234");
        submit.submit();
    }

    /*@After
    public void after() throws Exception {
       userService.cleanDB();
   }*/
}
