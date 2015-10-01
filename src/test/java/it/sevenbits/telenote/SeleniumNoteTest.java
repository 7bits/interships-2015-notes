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
public class SeleniumNoteTest {

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

//create a note
    @Test
    public void createNoteTest() {

        driver.findElement(By.id("js-addNote")).click();

        assertFalse(driver.findElements(By.className("js-note")).isEmpty());
}

//try to create a note without authorization
    @Test
    public void createNoteWithoutAuthorizationTest() {
        driver.get("http://127.0.0.1:9000");
        
        assertTrue(driver.findElements(By.className("js-addNote")).isEmpty());
}

//create a note and refresh page, assuming it will be saved
@Test
public void createNoteAndRefreshPageTest() {
    driver.findElement(By.id("js-addNote")).click();

    assertFalse(driver.findElements(By.className("js-note")).isEmpty());
    driver.navigate().refresh();
}

//creating and deleting note
    @Test
    public void createDeleteNoteTest() {
        driver.findElement(By.id("js-addNote")).click();

        assertFalse(driver.findElements(By.className("js-note")).isEmpty());

        Actions action = new Actions(driver);
        WebElement el = driver.findElement(By.className("js-note"));
        action.moveToElement(el);
        action.perform();

        WebDriverWait wait = new WebDriverWait(driver, 30);
        ExpectedCondition e = d -> {
            WebElement el1 = driver.findElement(By.className("js-note"));
            WebElement controlPanel = el1.findElement(By.className("js-control"));

            return !controlPanel.getCssValue("visibility").equals("hidden");
        };
        wait.until(e);

        WebElement button = driver.findElement(By.className("js-delBtn"));
        action = new Actions(driver);
        action.moveToElement(button);
        action.perform();
        button.click();

        wait = new WebDriverWait(driver, 30);
        ExpectedCondition elNotFound = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return driver.findElements(By.className("js-note")).isEmpty();
            }
        };
        wait.until(elNotFound);
    }

//creating and sharing note test
    @Test
    public void createShareNoteTest() {
      UserDetailsImpl user = new UserDetailsImpl();
      user.setUsername("warumweil@gmail.com");
      user.setName("J");
      try {
           user.setPassword((new BCryptPasswordEncoder()).encode("54321Qwerty"));

           repository.create(user);

           user.setPassword("54321Qwerty");
       } catch (Exception ex) {
           fail(ex.getMessage());
       }
        driver.findElement(By.id("js-addNote")).click();

        assertFalse(driver.findElements(By.className("js-note")).isEmpty());

	    clickShareButton(driver);

	    shareNote(driver);

        driver.findElement(By.id("logout")).click();

      WebElement email = driver.findElement(By.id("js-logText"));
      WebElement password = driver.findElement(By.className("js-logPass"));
      WebElement submit = driver.findElement(By.className("js-logSubmit"));

        email.sendKeys("warumweil@gmail.com");
      password.sendKeys("54321Qwerty");
        submit.submit();

        assertTrue(driver.getCurrentUrl().equals("http://127.0.0.1:9000/telenote"));

      try {
          repository.remove(user);
      } catch (Exception ex) {
          fail(ex.getMessage());
      }
  }

	/*@Test
    public void deleteSharingNoteTest() {
        UserDetailsImpl user = new UserDetailsImpl();
        user.setEmail("warumweil@gmail.com");
        user.setUsername("J");
        try {
             user.setPassword((new BCryptPasswordEncoder()).encode("54321Qwerty"));

             repository.create(user);

             user.setPassword("54321Qwerty");
         } catch (Exception ex) {
             fail(ex.getMessage());
         }

        driver.findElement(By.id("js-addNote")).click();

        assertFalse(driver.findElements(By.className("js-note")).isEmpty());

        WebElement content = driver.findElement(By.className("js-content"));
        content.click();
        WebElement text = driver.findElement(By.className("textarea"));
        text.sendKeys("Съешь этих мягких французских булок, да выпей же чаю");

        clickShareButton(driver);
	    shareNote(driver);
        clickShareButton(driver);
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        WebElement deleteShare = driver.findElement(By.className("deleteShare"));
        deleteShare.click();
        //driver.findElement(By.className("deleteShare")).click();

        try {
            repository.remove(user);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }*/

//create a note and type something
    @Test
    public void createTypeNoteTest() {
        driver.findElement(By.id("js-addNote")).click();

        assertFalse(driver.findElements(By.className("js-note")).isEmpty());

       WebElement content = driver.findElement(By.className("js-content"));
       content.click();
       WebElement text = driver.findElement(By.ByCssSelector.cssSelector(".textarea"));
       text.sendKeys("some text");
    }

    private void clickShareButton(WebDriver driver) {
        Actions action = new Actions(driver);
        WebElement el = driver.findElement(By.className("js-note"));
        action.moveToElement(el);
        action.perform();

        WebDriverWait wait = new WebDriverWait(driver, 30);
        ExpectedCondition e = d -> {
            WebElement el1 = driver.findElement(By.className("js-note"));
            WebElement controlPanel = el1.findElement(By.className("js-control"));
            return !controlPanel.getCssValue("visibility").equals("hidden");
        };
        wait.until(e);

        WebElement button = el.findElement(By.className("js-shaBtn"));
        action = new Actions(driver);
        action.moveToElement(button);
        action.perform();
        button.click();
	    el = driver.findElement(By.id("js-addShareEmail"));
	    el.sendKeys("warumweil@gmail.com");
	    driver.findElement(By.id("js-modalClose")).click();
    }
    private void shareNote(WebDriver driver) {

        org.openqa.selenium.interactions.Actions builder = new org.openqa.selenium.interactions.Actions(driver);
        driver.findElement(By.id("js-addShareEmail")).sendKeys("warumweil@gmail.com");
        builder.keyUp(Keys.CONTROL);
    	driver.findElement(By.id("js-addShare")).click();
    	driver.findElement(By.id("js-modalClose")).click();
    }
}
