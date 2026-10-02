package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CartPage{
    WebDriver driver;
    WebDriverWait wait;

    public CartPage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    By itemSet = By.cssSelector("[data-test='inventory-item-name']");
    By checkout = By.id("checkout");
    By continue_shopping =By.id("continue-shopping");

    public void removeItem(String product){
        driver.findElement(By.id("remove-sauce-labs-"+product)).click();
    }

    public void getAllItems(){
        List<WebElement> items = driver.findElements(itemSet);
//        System.out.println("Displaying all items in cart");
//        for(WebElement i:items)
//            System.out.println(i.getText());
//        System.out.println(items.size());
    }

    public void clickCheckOut(){
        driver.findElement(checkout).click();
    }

    public void Continue_shopping(){
        driver.findElement(continue_shopping).click();
    }
}
