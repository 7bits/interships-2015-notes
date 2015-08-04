package it.sevenbits.springboottutorial;

import it.sevenbits.springboottutorial.core.domain.UserDetailsImpl;
import it.sevenbits.springboottutorial.core.repository.User.IUserRepository;
import org.junit.*;
import org.junit.runner.RunWith;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.interactions.Actions;
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
public class SeleniumNoteTest {

    private static WebDriver driver;

    private static UserDetailsImpl user;

    @Autowired
    @Qualifier(value = "theUserPersistRepository")
    public IUserRepository repository;


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

        WebElement element = driver.findElement(By.className("js-enter"));

        assertTrue(element.isEnabled());
        assertTrue(element.isDisplayed());

        element.click();

        WebElement email = driver.findElement(By.ByCssSelector.cssSelector("form[name=signinForm] input[name=username]"));
        WebElement password = driver.findElement(By.ByCssSelector.cssSelector("form[name=signinForm] input[name=password]"));
        WebElement submit = driver.findElement(By.ByCssSelector.cssSelector("form[name=signinForm] .loginSubmit"));

        email.sendKeys(user.getEmail());
        password.sendKeys(user.getPassword());
        submit.submit();

        assertEquals(driver.getCurrentUrl(), "http://127.0.0.1:9000/telenote");
    }
    @Test
    public void createDeleteNoteTest() {
        driver.findElement(By.className("addNote")).click();

        assertFalse(driver.findElements(By.className("cell")).isEmpty());

        Actions builder = new Actions(driver);
        builder.moveToElement(driver.findElement(By.className("control"))).perform();
        By locator = By.className("delBtn");
        driver.findElement(locator).click();
        assertTrue(driver.findElements(By.className("cell")).isEmpty());
        

    }
    /*@Test
    public void deleteNoteTest() {
        driver.findElement(By.className("addNote")).click();

        assertFalse(driver.findElements(By.className("cell")).isEmpty());

    }*/
}
