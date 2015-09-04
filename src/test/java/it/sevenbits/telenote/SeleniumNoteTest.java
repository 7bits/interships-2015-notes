package it.sevenbits.telenote;

import it.sevenbits.telenote.core.domain.UserDetailsImpl;
import it.sevenbits.telenote.core.repository.User.IUserRepository;
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
import org.flywaydb.test.annotation.FlywayTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
@FlywayTest
public class SeleniumNoteTest {

    private static WebDriver driver;

    private static UserDetailsImpl user;

    @Autowired
    @Qualifier(value = "theUserPersistRepository")
    public IUserRepository repository;


    @BeforeClass
    public static void initDriver() {
        driver = new FirefoxDriver();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        user = new UserDetailsImpl();
        user.setEmail("ololo@ololo.com");
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

        WebElement email = driver.findElement(By.ByCssSelector.cssSelector("form[name=signinForm] input[name=username]"));
        WebElement password = driver.findElement(By.ByCssSelector.cssSelector("form[name=signinForm] input[name=password]"));
        WebElement submit = driver.findElement(By.ByCssSelector.cssSelector("form[name=signinForm] .loginSubmit"));

        email.sendKeys(user.getEmail());
        password.sendKeys(user.getPassword());
        submit.submit();

        assertTrue(driver.getCurrentUrl().equals("http://127.0.0.1:9000/telenote"));
    }

    @After
	public void after() throws Exception {
        try {
            repository.remove(user);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void createDeleteNoteTest() {
        driver.findElement(By.className("test-addNote")).click();

        assertFalse(driver.findElements(By.className("test-note")).isEmpty());

        Actions action = new Actions(driver);
        WebElement el = driver.findElement(By.className("test-note"));
        action.moveToElement(el);
        action.perform();

        WebDriverWait wait = new WebDriverWait(driver, 30);
        ExpectedCondition e = d -> {
            WebElement el1 = driver.findElement(By.className("test-note"));
            WebElement controlPanel = el1.findElement(By.className("test-control"));

            return !controlPanel.getCssValue("visibility").equals("hidden");
        };
        wait.until(e);

        WebElement button = driver.findElement(By.className("test-delBtn"));
        action = new Actions(driver);
        action.moveToElement(button);
        action.perform();
        button.click();

        wait = new WebDriverWait(driver, 30);
        ExpectedCondition elNotFound = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return driver.findElements(By.className("test-note")).isEmpty();
            }
        };
        wait.until(elNotFound);

    }

    @Test
    public void userNameTest() {
        driver.findElement(By.className("test-user")).click();

        assertTrue(driver.getCurrentUrl().equals("http://127.0.0.1:9000/account"));

        WebElement toClear = driver.findElement(By.className("test-username"));
        toClear.sendKeys(Keys.CONTROL + "a");
        toClear.sendKeys(Keys.DELETE);
  	    toClear.sendKeys("J");

       WebElement button = driver.findElement(By.className("test-submit"));
        Actions action = new Actions(driver);
        action.moveToElement(button);
        action.perform();
        button.click();
    }

    @Test
    public void userPasswordTest() {
        driver.findElement(By.className("test-user")).click();

        assertTrue(driver.getCurrentUrl().equals("http://127.0.0.1:9000/account"));

        WebElement toClear = driver.findElement(By.className("test-currentPass"));
	    toClear.sendKeys(Keys.CONTROL + "a");
        toClear.sendKeys(Keys.DELETE);
  	    toClear.sendKeys("Ololo73");
	    driver.findElement(By.className("test-newPass")).sendKeys("Capitan1234");
  	
        WebElement button = driver.findElement(By.className("test-submit"));
        Actions action = new Actions(driver);
        action.moveToElement(button);
        action.perform();
        button.click();
    }

    @Test
    public void userDesignTest() {
        driver.findElement(By.className("test-user")).click();

        assertTrue(driver.getCurrentUrl().equals("http://127.0.0.1:9000/account"));

  	    WebElement button = driver.findElement(By.id("lightDen"));
        Actions action = new Actions(driver);
        action.moveToElement(button);
        action.perform();
        button.click();

	    driver.findElement(By.className("test-submit")).click();
    }

    @Test
    public void createShareNoteTest() {
      UserDetailsImpl user = new UserDetailsImpl();
      user.setEmail("warumweil@gmail.com");
      user.setName("J");
      try {
           user.setPassword((new BCryptPasswordEncoder()).encode("54321Qwerty"));

           repository.create(user);

           user.setPassword("54321Qwerty");
       } catch (Exception ex) {
           fail(ex.getMessage());
       }
        driver.findElement(By.className("test-addNote")).click();

        assertFalse(driver.findElements(By.className("test-note")).isEmpty());

	    clickShareButton(driver);

	    shareNote(driver);

        driver.findElement(By.id("logout")).click();

      WebElement email = driver.findElement(By.ByCssSelector.cssSelector("form[name=signinForm] input[name=username]"));
      WebElement password = driver.findElement(By.ByCssSelector.cssSelector("form[name=signinForm] input[name=password]"));
      WebElement submit = driver.findElement(By.ByCssSelector.cssSelector("form[name=signinForm] .loginSubmit"));

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

        driver.findElement(By.className("test-addNote")).click();

        assertFalse(driver.findElements(By.className("test-note")).isEmpty());

        WebElement content = driver.findElement(By.className("test-content"));
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

    @Test
    public void createTypeNoteTest() {
        driver.findElement(By.className("test-addNote")).click();

        assertFalse(driver.findElements(By.className("test-note")).isEmpty());

       WebElement content = driver.findElement(By.className("test-content"));
       content.click();
       WebElement text = driver.findElement(By.ByCssSelector.cssSelector(".textarea"));
       text.sendKeys("some text");
    }

    private void clickShareButton(WebDriver driver) {
        Actions action = new Actions(driver);
        WebElement el = driver.findElement(By.className("test-note"));
        action.moveToElement(el);
        action.perform();

        WebDriverWait wait = new WebDriverWait(driver, 30);
        ExpectedCondition e = d -> {
            WebElement el1 = driver.findElement(By.className("test-note"));
            WebElement controlPanel = el1.findElement(By.className("test-control"));
            return !controlPanel.getCssValue("visibility").equals("hidden");
        };
        wait.until(e);

        WebElement button = el.findElement(By.className("test-shaBtn"));
        action = new Actions(driver);
        action.moveToElement(button);
        action.perform();
        button.click();
	    el = driver.findElement(By.className("test-addShareEmail"));
	    el.sendKeys("warumweil@gmail.com");
	    driver.findElement(By.className("test-shareApply")).click();
    }
    private void shareNote(WebDriver driver) {
        WebElement el = driver.findElement(By.className("test-addShareEmail"));
    	el.sendKeys("warumweil@gmail.com");
    	driver.findElement(By.className("test-addShare")).click();
    	driver.findElement(By.className("test-shareApply")).click();
    }
}
