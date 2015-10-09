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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.URL;
import java.net.MalformedURLException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class SeleniumRegistrationTest {
    public static final String USERNAME = "julianovikova";
    public static final String ACCESS_KEY = "685e7716-f83b-4806-949b-5d5087614606";
    public static final String URL = "http://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:80/wd/hub";

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

    /*private void findInputFields() {
        email = driver.findElement(By.xpath("/html/body/div/div[3]/form/input[2]"));
        username = driver.findElement(By.xpath("/html/body/div/div[3]/form/input[3]"));
        password = driver.findElement(By.xpath("/html/body/div/div[3]/form/input[4]"));
        submit = driver.findElement(By.xpath("/html/body/div/div[3]/form/button"));
    }*/

    /*@Before
    public void before() throws Exception {
        driver.get("https://notes:bestpassword@tele-notes.7bits.it/");
        findInputFields();

        //register user
        email.sendKeys(user.getUsername());
        username.sendKeys(user.getName());
        password.sendKeys(user.getPassword());

        submit.submit();

        assertEquals("http://127.0.0.1:9000/signup", driver.getCurrentUrl());
        driver.findElement(By.className("js-backToMain")).click();

        findInputFields();
    }*/

    /*@After
    public void after() throws Exception {
        //submit.submit();
        //submit.click();

        //assertEquals(driver.getCurrentUrl(), "http://127.0.0.1:9000/signup");
        //List<WebElement> error = driver.findElements(By.className("errorDiv"));
        //assertFalse(error.isEmpty());

        try {
            userService.cleanDB();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }*/

//register new user
    @Test
    public void regAllValidTest() throws Exception {
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

        //findInputFields();

        //register user
        /*email.sendKeys(user.getUsername());
        username.sendKeys(user.getName());
        password.sendKeys(user.getPassword());

        submit.submit();

        assertEquals("http://127.0.0.1:9000/signup", driver.getCurrentUrl());
        driver.findElement(By.className("js-backToMain")).click();*/

        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[2]")).sendKeys("julia.novikova@gmail.com");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[3]")).sendKeys("j");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[4]")).sendKeys("12345Qwerty");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/button")).click();
        assertEquals("https://notes:bestpassword@tele-notes.7bits.it/signup", driver.getCurrentUrl());
        driver.findElement(By.xpath("/html/body/div/div[2]/div/div[5]/a")).click();
        assertEquals("https://notes:bestpassword@tele-notes.7bits.it/", driver.getCurrentUrl());


        /*email.sendKeys(user.getUsername());
        username.sendKeys(user.getName());
        password.sendKeys(user.getPassword());
        submit.submit();*/

    try {
        userService.cleanDB();
    } catch (Exception ex) {
        fail(ex.getMessage());
    }
        driver.quit();
}

//register user with invalid email
    @Test
    public void regInvalidEmailTest() throws Exception {
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
        //WebElement email = driver.findElement(By.name("email"));
        //findInputFields();

        /*email.sendKeys("ololo");
        username.click();
        username.sendKeys(user.getName());
        password.sendKeys(user.getPassword());
        submit.submit();*/

        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[2]")).sendKeys("ololo");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[3]")).sendKeys("j");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[4]")).sendKeys("12345Qwerty");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/button")).click();

        driver.findElement(By.className("js-emailError"));

        try {
            userService.cleanDB();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
            driver.quit();

    }

//register almost existing user
    @Test
    public void regUserExistsTest() throws Exception {
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
        //findInputFields();
        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[2]")).sendKeys("ololo@ololo.com");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[3]")).sendKeys("j");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[4]")).sendKeys("12345Qwerty");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/button")).click();

        driver.findElement(By.className("js-EmailMess"));

        try {
            userService.cleanDB();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
            driver.quit();
    }

//register user with wrong username
    @Test
    public void regWrongUsernameTest() throws Exception {
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

        //findInputFields();
        /*email.sendKeys("ololo1@ololo.com");
        username.sendKeys("");
        password.sendKeys(user.getPassword());
        submit.submit();*/

        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[2]")).sendKeys("ololo@ololo.com");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[3]")).sendKeys("");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[4]")).sendKeys("12345Qwerty");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/button")).click();

	    driver.findElement(By.className("js-nameError"));

        try {
            userService.cleanDB();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
            driver.quit();
    }


//register user with username made of numbers
    @Test
    public void regNumberUsernameTest() throws Exception {
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

        //findInputFields();
        /*email.sendKeys("ololo2@ololo.com");
        username.sendKeys("1234");
        password.sendKeys(user.getPassword());
        submit.submit();*/
        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[2]")).sendKeys("ololo@ololo.com");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[3]")).sendKeys("1234");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[4]")).sendKeys("12345Qwerty");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/button")).click();

        try {
            userService.cleanDB();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
            driver.quit();
    }

//register user with empty password
    @Test
    public void regEmptyPasswordTest() throws Exception {
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

        //findInputFields();
        /*email.sendKeys("ololo3@ololo.com");
        username.sendKeys("Leo");
        password.sendKeys("");
        submit.submit();*/
        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[2]")).sendKeys("ololo@ololo.com");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[3]")).sendKeys("Leo");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[4]")).sendKeys("");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/button")).click();

        driver.findElement(By.className("js-passError"));
    }

//register user with password made of spaces
   @Test
    public void regSpacesPasswordTest() throws Exception {
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
        //findInputFields();
        /*email.sendKeys("ololo4@ololo.com");
        username.sendKeys("Leo");
        password.sendKeys("        ");
        submit.submit();*/

        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[2]")).sendKeys("ololo@ololo.com");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[3]")).sendKeys("Leo");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[4]")).sendKeys("       ");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/button")).click();

        driver.findElement(By.className("js-passError"));
    }

//register user with short password
    @Test
    public void regShortPasswordTest() throws Exception {
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

        //findInputFields();
        /*email.sendKeys("ololo5@ololo.com");
        username.sendKeys("Leo");
        password.sendKeys("1");
        submit.submit();*/
        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[2]")).sendKeys("ololo@ololo.com");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[3]")).sendKeys("Leo");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[4]")).sendKeys("1");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/button")).click();

        driver.findElement(By.className("js-passError"));
    }

//register user with invalid password
    @Test
    public void regWrongPasswordTest() throws Exception {
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
        //findInputFields();
        /*email.sendKeys("ololo6@ololo.com");
        username.sendKeys("Leo");
        password.sendKeys("123");
        submit.submit();*/
        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[2]")).sendKeys("ololo@ololo.com");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[3]")).sendKeys("Leo");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/input[4]")).sendKeys("123");
        driver.findElement(By.xpath("/html/body/div/div[3]/form/button")).click();

        driver.findElement(By.className("js-passError"));
    }

    /*@BeforeClass
    public static void initDriver() {
        //driver = new ChromeDriver();
        driver = new FirefoxDriver();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        user = new UserDetailsImpl();
        user.setUsername("ololo@ololo.com");
        user.setName("Capitan");
        user.setPassword("Ololo73");
        //driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
    }*/

    /*AfterClass
    public static void closeDriver() {
        driver.quit();
    }*/
}
