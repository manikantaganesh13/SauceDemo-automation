package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.gherkin.model.Given;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class E2ETests extends BaseTest{

    private static final Logger log = LoggerFactory.getLogger(E2ETests.class);
    String backpack = "backpack";
    String bikeLight = "bike-light";
    String tshirt = "bolt-t-shirt";
    String jacket = "fleece-jacket";
    String onsie = "onesie";

//    @Test
//    public void test(){
//        ExtentSparkReporter spark =
//                new ExtentSparkReporter(
//                        "Reports/ExtentReport.html"
//                );
//
//        ExtentReports extent = new ExtentReports();
//
//        extent.attachReporter(spark);
//
//        ExtentTest test =
//                extent.createTest("Sample Test");
//
//        test.info("Test execution started");
//
//        test.pass("Test passed successfully");
//
//        extent.flush();
//
//        System.out.println("Report generated successfully!");
//
//    }

    @Test
    public void singleItemPurchase(){
        LoginPage login = new LoginPage(driver);
        login.doLogin("standard_user","secret_sauce");

        InventoryPage inventory = new InventoryPage(driver);
        inventory.isLoaded();
        inventory.addToCart("backpack");
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

    @Test
    public void sortByPriceThenBuyCheapest(){
        new LoginPage(driver).doLogin("standard_user","secret_sauce");

        InventoryPage inventory = new InventoryPage(driver);
        inventory.isLoaded();
        //click sort dd and select price low to high
        inventory.sortBy("Price (low to high)");

        List<BigDecimal> displayedPrices = inventory.getAllPricesAsDecimal();
        List<BigDecimal> expectedOrder = new ArrayList<>(displayedPrices);
        Collections.sort(expectedOrder);
        Assert.assertEquals(displayedPrices,expectedOrder,"Products are not sorted by price (low to high)");

        BigDecimal cheapestPrice = Collections.min(displayedPrices);
        Assert.assertEquals(displayedPrices.get(0), cheapestPrice,"First product is not the cheapest one");

        String pickedName = inventory.addFirstProductToCart();

        inventory.openCart();
        CartPage cart = new CartPage(driver);
        cart.clickCheckOut();

        CheckoutPage checkout = new CheckoutPage(driver);
        checkout.fillInfo("Harry","Brook","456789");
        checkout.clickContinue();

        BigDecimal expectedTax = cheapestPrice.multiply(TestData.TAX_RATE)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal expectedTotal = cheapestPrice.add(expectedTax);

        Assert.assertEquals(checkout.getSubTotal(), cheapestPrice, "Item total mismatch");
        Assert.assertEquals(checkout.getTax(), expectedTax, "Tax mismatch");
        Assert.assertEquals(checkout.getTotal(), expectedTotal, "Total mismatch");

        checkout.clickFinish();
        Assert.assertEquals(checkout.getConfirmationMessage(), "Thank you for your order!");

    }

    @Test
    public void checkoutErrorThenRecover(){
        new LoginPage(driver).doLogin("standard_user","secret_sauce");

        InventoryPage inventory = new InventoryPage(driver);
        inventory.isLoaded();
        inventory.addToCart(backpack).openCart();

        CartPage cart = new CartPage(driver);
        cart.clickCheckOut();

        CheckoutPage checkout = new CheckoutPage(driver);
        checkout.clickContinue();
        Assert.assertTrue(checkout.getErrorMessage().contains("First Name is required"));

        checkout.fillInfo("John","","");
        checkout.clickContinue();
        Assert.assertTrue(checkout.getErrorMessage().contains("Last Name is required"));

        checkout.fillInfo("John","Doe","");
        checkout.clickContinue();
        Assert.assertTrue(checkout.getErrorMessage().contains("Postal Code is required"));

        checkout.fillInfo("John","Doe","13234");
        checkout.clickContinue();
        checkout.clickFinish();
        Assert.assertEquals(checkout.getConfirmationMessage(),"Thank you for your order!");
    }

    @Test
    public void loggedOutUserCannotOpenCheckoutDirectly(){
        new LoginPage(driver).doLogin("standard_user","secret_sauce");

        InventoryPage inventory = new InventoryPage(driver);
        inventory.isLoaded();
        inventory.addToCart(backpack).openCart();

        CartPage cart = new CartPage(driver);
        cart.clickCheckOut();

        inventory.logout();

        driver.get("https://www.saucedemo.com/checkout-step-two.html");
        Assert.assertTrue(new LoginPage(driver).getErrorMessage().contains("You can only access '/checkout-step-two.html' when you are logged in"));
    }
}
