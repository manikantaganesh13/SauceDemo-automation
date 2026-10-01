package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginPage;

import java.time.Duration;

public class LoginTest extends BaseTest{

    LoginPage login;

    @Test
    public void loginTest(){
        login = new LoginPage(driver);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        login.doLogin("standard_user","secret_sauce");
        Assert.assertTrue(true, "Login failed");
    }

    @Test
    public void loginInvalid(){
        login = new LoginPage(driver);
        login.doLogin("standard_user","123456789");
        Assert.assertTrue(true, "Login failed because of wrong credentials");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
    }

}
