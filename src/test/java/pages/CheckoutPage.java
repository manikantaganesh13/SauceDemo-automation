package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.math.BigDecimal;
import java.time.Duration;

public class CheckoutPage {
    WebDriver driver;
    WebDriverWait wait;

    public CheckoutPage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    By firstName = By.id("first-name");
    By lastName = By.id("last-name");
    By postalCode = By.id("postal-code");
    By continue_btn = By.id("continue");
    By cancel = By.id("cancel");
    By finishBtn = By.id("finish");
    By completeHeader = By.cssSelector("[data-test='complete-header']");
    By back_home = By.id("back-to-products");
    By subTotal = By.className("summary_subtotal_label");
    By tax = By.className("summary_tax_label");
    By total = By.className("summary_total_label");

    public void fillInfo(String first,String last,String pcode){
        type(firstName,first);
        type(lastName,last);
        type(postalCode,pcode);
    }

    public void type(By locator,String value){
        driver.findElement(locator).sendKeys(value);
    }

    public void clickContinue(){
        driver.findElement(continue_btn).click();
    }

    public void clickFinish(){
        driver.findElement(finishBtn).click();
    }

    public String getConfirmationMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(completeHeader)).getText();
    }

    public void clickBack(){
        driver.findElement(back_home).click();
    }

    public BigDecimal getSubTotal(){
        String amount = driver.findElement(subTotal).getText();
        return new BigDecimal(amount.substring(amount.indexOf('$')+1));
    }

    public BigDecimal getTax(){
        String amount = driver.findElement(tax).getText();
        return new BigDecimal(amount.substring(amount.indexOf('$')+1));
    }

    public BigDecimal getTotal(){
        String amount = driver.findElement(total).getText();
        return new BigDecimal(amount.substring(amount.indexOf('$')+1));
    }
}
