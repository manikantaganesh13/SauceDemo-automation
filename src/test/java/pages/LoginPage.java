package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    WebDriver driver;
    WebDriverWait wait;

    public LoginPage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    By username = By.xpath("//input[@id='user-name']");
    By password = By.xpath("//input[@id='password']");
    By login_btn = By.xpath("//input[@id='login-button']");
    By error = By.cssSelector("[data-test='error']");


    public void doLogin(String uname,String pwd){
        wait.until(ExpectedConditions.elementToBeClickable(username)).sendKeys(uname);
        wait.until(ExpectedConditions.elementToBeClickable(password)).sendKeys(pwd);
        wait.until(ExpectedConditions.elementToBeClickable(login_btn)).click();
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(error)).getText();
    }
}
