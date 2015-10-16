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

        driver.get("http://127.0.0.1:9000/");
    }

//resetting password with valid email
    @Test
    public void resetPasswordTest() {
        driver.findElement(By.xpath("/html/body/div/div[2]/form/div[2]/a")).click();
        assertEquals("http://127.0.0.1:9000/resetpass", driver.getCurrentUrl());
        email = driver.findElement(By.xpath("/html/body/div/div[2]/form/input[2]"));
        email.sendKeys("bits@ololo.com");
        submit = driver.findElement(By.xpath("/html/body/div/div[2]/form/button"));
        submit.submit();
    }

//trying to reset password with invalid email
    @Test
    public void resetInvalidEmailPasswordTest() {
        driver.findElement(By.xpath("/html/body/div/div[2]/form/div[2]/a")).click();
        assertEquals("http://127.0.0.1:9000/resetpass", driver.getCurrentUrl());
        email = driver.findElement(By.xpath("/html/body/div/div[2]/form/input[2]"));
        email.sendKeys("Qwerty");
        submit = driver.findElement(By.xpath("/html/body/div/div[2]/form/button"));
        submit.submit();

        driver.findElement(By.xpath("/html/body/div/div[2]/form/div[2]/span"));
    }

//trying to reset password with empty field
    @Test
    public void resetEmptyEmailPasswordTest() {
        driver.findElement(By.xpath("/html/body/div/div[2]/form/div[2]/a")).click();
        assertEquals("http://127.0.0.1:9000/resetpass", driver.getCurrentUrl());
        email = driver.findElement(By.xpath("/html/body/div/div[2]/form/input[2]"));
        email.sendKeys(" ");
        submit = driver.findElement(By.xpath("/html/body/div/div[2]/form/button"));
        submit.submit();

        driver.findElement(By.xpath("/html/body/div/div[2]/form/div[2]/span"));
    }

    @After
    public void after() throws Exception {
    try {
        userService.cleanDB();
    } catch (Exception ex) {
        fail(ex.getMessage());
   }
}
}
