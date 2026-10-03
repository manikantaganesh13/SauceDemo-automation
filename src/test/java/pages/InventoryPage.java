package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryPage extends BasePage{
    WebDriver driver;
    WebDriverWait wait;

    By title = By.cssSelector("[data-test='title]");
    By cartLink = By.className("shopping_cart_link");
    By cartBadge = By.className("shopping_cart_badge");
    By menuBtn = By.id("react-burger-menu-btn");
    By logoutLink = By.id("logout_sidebar_link");
    By sortDropDown = By.className("product_sort_container");
    By itemCards = By.className("inventory_item");
    By itemPrices = By.className("inventory_item_price");

    By itemNames = By.className("inventory_item_name");



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
        wait.until(ExpectedConditions.elementToBeClickable(addButton(product))).click();
        return this;
    }

    public InventoryPage removeFromCart(String product){
        wait.until(ExpectedConditions.elementToBeClickable(removeButton(product))).click();
        return this;

    }

    public void openCart(){
        driver.findElement(cartLink).click();
    }

    public int getCartCount(){
        List<WebElement> badge = driver.findElements(cartBadge);
        return badge.isEmpty() ? 0 : Integer.parseInt(badge.get(0).getText());
    }

    public void logout() {
        driver.findElement(menuBtn).click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
    }

    public InventoryPage sortBy(String value){
        new Select(driver.findElement(sortDropDown)).selectByVisibleText(value);
        return this;
    }

    public List<BigDecimal> getAllPricesAsDecimal(){
//       List<WebElement> prices = driver.findElements(itemPrices);
//        System.out.println("displayng all priices");
//       for(WebElement i:prices)
//           System.out.println(i.getText().substring(1));

        return driver.findElements(itemPrices).stream()
                .map(e -> new BigDecimal(e.getText().substring(1)))
                .collect(Collectors.toList());
    }

    public String addFirstProductToCart() {
        WebElement firstProduct = wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(itemCards))).get(0);
        firstProduct.findElement(By.tagName("button")).click();
        return firstProduct.findElement(itemNames).getText();
    }
}
