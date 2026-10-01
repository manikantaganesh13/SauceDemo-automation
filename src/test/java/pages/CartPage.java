package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CartPage{
    WebDriver driver;
    WebDriverWait wait;

    public CartPage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    By checkout = By.id("checkout");
    By continue_shopping =By.id("continue-shopping");

    public void clickCheckOut(){
        driver.findElement(checkout).click();
    }

    public void Continue_shopping(){
        driver.findElement(continue_shopping).click();
    }
}
