package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CheckoutPage;
import pages.InventoryPage;
import pages.LoginPage;
import pages.CartPage;
import utils.TestData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;

public class E2ETests extends BaseTest{

    private static final Logger log = LoggerFactory.getLogger(E2ETests.class);
    String backpack = "backpack";
    String bikeLight = "bike-light";
    String tshirt = "bolt-t-shirt";
    String jacket = "fleece-jacket";
    String onsie = "onesie";

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

    @Test
    public void addRemoveThenBuyRemainingItem(){
        LoginPage login = new LoginPage(driver);
        login.doLogin("standard_user","secret_sauce");

        InventoryPage inventory = new InventoryPage(driver);
        inventory.isLoaded();
        inventory.addToCart(backpack).addToCart(bikeLight).addToCart(tshirt).addToCart(jacket);
        inventory.openCart();
        Assert.assertEquals(inventory.getCartCount(),4);
        System.out.println(inventory.getCartCount());

        //Remove jacket on inventory
        inventory.removeFromCart(jacket);
        Assert.assertEquals(inventory.getCartCount(),3);
        System.out.println(inventory.getCartCount());

        inventory.openCart();
        CartPage cart = new CartPage(driver);
        //Remove bikeLight and backpack from cart
        cart.removeItem(bikeLight);
        cart.removeItem(backpack);

        cart.clickCheckOut();
        CheckoutPage checkout = new CheckoutPage(driver);
        checkout.fillInfo("user","name","123465");
        checkout.clickContinue();
        checkout.clickFinish();
        Assert.assertEquals(checkout.getConfirmationMessage(), "Thank you for your order!");

    }

    //price check
    @Test
    public void priceValidation(){
        new LoginPage(driver).doLogin("standard_user","secret_sauce");

        InventoryPage inventory = new InventoryPage(driver);
        inventory.isLoaded();
        inventory.addToCart(backpack).addToCart(onsie).addToCart(tshirt).openCart();

        CartPage cart = new CartPage(driver);
        cart.clickCheckOut();

        CheckoutPage checkout = new CheckoutPage(driver);
        checkout.fillInfo("Sam","ternus","877655");
        checkout.clickContinue();

        BigDecimal expectedSubTotal = TestData.PRICES.get(backpack)
                .add(TestData.PRICES.get(onsie))
                .add(TestData.PRICES.get(tshirt));

        BigDecimal expectedTax = expectedSubTotal.multiply(new BigDecimal(0.08)).
                setScale(2, RoundingMode.HALF_UP);

        Assert.assertEquals(checkout.getSubTotal(),expectedSubTotal,"Item total mismatch");
        Assert.assertEquals(checkout.getTax(),expectedTax,"Tax mismatch");
        Assert.assertEquals(checkout.getTotal(),expectedSubTotal.add(expectedTax),"Total mismatch");

        checkout.clickFinish();
        Assert.assertEquals(checkout.getConfirmationMessage(),"Thank you for your order!");
    }

    //Two consecutive orders
    @Test
    public void twoConsecutiveOrders(){
        new LoginPage(driver).doLogin("standard_user","secret_sauce");

        InventoryPage inventory = new InventoryPage(driver);
        inventory.isLoaded();
        inventory.addToCart(backpack).openCart();

        CartPage cart = new CartPage(driver);
        cart.clickCheckOut();

        CheckoutPage checkout = new CheckoutPage(driver);
        checkout.fillInfo("John","Smith","123456");
        checkout.clickContinue();
        checkout.clickFinish();
        Assert.assertEquals(inventory.getCartCount(),0,"Cart count should be empty after order");
        checkout.clickBack();

        inventory.addToCart(onsie).openCart();
        cart.clickCheckOut();
        checkout.fillInfo("John","Smith","123456");
        checkout.clickContinue();
        checkout.clickFinish();
        Assert.assertEquals(checkout.getConfirmationMessage(),"Thank you for your order!");
    }
}
