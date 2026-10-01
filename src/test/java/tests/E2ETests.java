package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CheckoutPage;
import pages.InventoryPage;
import pages.LoginPage;
import pages.CartPage;

public class E2ETests extends BaseTest{

    String backpack = "backpack";
    String bikeLight = "bike-light";
    String tshirt = "bolt-t-shirt";
    String jacket = "fleece-jacket";

    @Test
    public void singleItemPurchase(){
        LoginPage login = new LoginPage(driver);
        login.doLogin("standard_user","secret_sauce");

        InventoryPage inventory = new InventoryPage(driver);
        inventory.isLoaded();
        inventory.addToCart("backpack");
//        inventory.addToCart();
        inventory.openCart();

        CartPage cart = new CartPage(driver);
        cart.clickCheckOut();

        CheckoutPage checkout = new CheckoutPage(driver);
        checkout.fillInfo("peter","parker","13456");
        checkout.clickContinue();
        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-two"));
        checkout.clickFinish();

        Assert.assertEquals(checkout.getConfirmationMessage(), "Thank you for your order!");
        checkout.clickBack();

        inventory.logout();
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/");

    }
}
