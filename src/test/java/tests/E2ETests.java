package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
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
        Assert.assertTrue(inventory.isLoaded());
        inventory.addToCart("backpack");
        inventory.openCart();

        CartPage cart = new CartPage(driver);

    }
}
