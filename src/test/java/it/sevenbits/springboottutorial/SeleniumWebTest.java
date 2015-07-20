package it.sevenbits.springboottutorial;

//import it.sevenbits.springboottutorial.core.repository.User.IUserRepository;
//import it.sevenbits.springboottutorial.core.repository.User.UserRepository;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class SeleniumWebTest {

    private static WebDriver driver;

    @Test
    public void qElementTest() throws Exception {
        driver.get("http://www.google.com");
        WebElement element = driver.findElement(By.name("q"));

        assertTrue("Is q element exist?", element.isEnabled());
    }

    @BeforeClass
    public static void initDriver() {
        //driver = new ChromeDriver();
        driver = new FirefoxDriver();
    }

    @AfterClass
    public static void closeDriver() {
        driver.close();
    }
}
