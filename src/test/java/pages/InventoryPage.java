package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class InventoryPage extends BasePage{
    WebDriver driver;
    WebDriverWait wait;

    By title = By.cssSelector("[data-test='title]");
    By cartLink = By.className("shopping_cart_link");
    By cartBadge = By.className("shopping_cart_badge");
    By menuBtn = By.id("react-burger-menu-btn");
    By logoutLink = By.id("logout_sidebar_link");

    public InventoryPage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void isLoaded() {
         wait.until(ExpectedConditions.urlContains("inventory.html"));
    }

    public By addButton(String product){
        return By.id("add-to-cart-sauce-labs-"+product);
    }

    public By removeButton(String product){
        return By.id("remove-sauce-labs-"+product);
    }

    public InventoryPage addToCart(String product){
        System.out.println("inside add to cart ");
        wait.until(ExpectedConditions.elementToBeClickable(addButton(product))).click();
        System.out.println("added toc cart");
        return this;
    }

    public InventoryPage removeFromCart(String product){
        wait.until(ExpectedConditions.elementToBeClickable(removeButton(product))).click();
        return this;
    }

    public void openCart(){
        driver.findElement(cartLink).click();
    }

    public void logout() {
        driver.findElement(menuBtn).click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
    }
}
