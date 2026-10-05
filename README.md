# SauceDemo Automation

UI test automation framework for the [Sauce Demo](https://www.saucedemo.com/) e-commerce site, built with **Java 21**, **Selenium WebDriver**, **TestNG**, and **Extent Reports**. It uses the **Page Object Model (POM)** and covers complete customer journeys from login through checkout.

## Tech Stack

| Component       | Technology             | Version |
| --------------- |------------------------| ------- |
| Language        | Java                   | 21      |
| Build tool      | Maven                  | 3.6+    |
| UI automation   | Selenium WebDriver     | 4.50.0  |
| Test framework  | TestNG                 | 7.10.2  |
| Reporting       | Extent Reports (Spark) | 5.1.2   |
| Browser         | Google Chrome          | Latest  |

## Project Structure

```
SauceDemo-automation
├── Reports/
│   └── ExtentReport.html          # Generated HTML test report
├── src
│   ├── main/java/org/example
│   │   └── Main.java              # Placeholder entry point
│   └── test/java
│       ├── listeners/
│       │   └── TestListener.java  # TestNG listener -> writes results to Extent report
│       ├── pages/                 # Page Object classes
│       │   ├── BasePage.java
│       │   ├── LoginPage.java
│       │   ├── InventoryPage.java
│       │   ├── CartPage.java
│       │   └── CheckoutPage.java
│       ├── tests/
│       │   ├── BaseTest.java      # Driver setup / teardown
│       │   ├── E2ETests.java      # End-to-end purchase scenarios
│       │   └── LoginTest.java     # Login tests
│       └── utils/
│           ├── ExtentReportManager.java  # Extent report setup (thread-safe)
│           └── TestData.java             # Product prices and tax rate
├── pom.xml
├── testng.xml                     # TestNG suite definition
└── .gitignore
```

## Test Scenarios

The suite defined in `testng.xml` runs these tests from `tests.E2ETests`:

| Test | What it verifies |
| ---- | ---------------- |
| `singleItemPurchase` | Log in, buy one item, confirm the "Thank you for your order!" message, then log out |
| `addRemoveThenBuyRemainingItem` | Add four items, remove some from the inventory page and the cart, check cart counts, and buy the remaining item |
| `priceValidation` | Item total, 8% tax, and grand total on the checkout overview match the expected values |
| `twoConsecutiveOrders` | Place two orders back-to-back in one session, and confirm the cart is empty after the first |
| `sortByPriceThenBuyCheapest` | Sort by "Price (low to high)", verify the order, buy the cheapest item, and validate its totals |
| `checkoutErrorThenRecover` | Check the First Name, Last Name, and Postal Code validation errors, then recover and complete the order |
| `loggedOutUserCannotOpenCheckoutDirectly` | After logging out, opening a checkout URL directly shows the "must be logged in" error |

`tests.LoginTest` (valid and invalid login) is also in the project but is not included in `testng.xml`.

## Prerequisites

- **JDK 21**
- **Apache Maven 3.6+**
- **Microsoft Edge** (Selenium Manager downloads the matching driver automatically)
- Internet access to `https://www.saucedemo.com/`

Check your setup:

```bash
java -version
mvn -version
```

## Getting Started

```bash
git clone https://github.com/manikantaganesh13/SauceDemo-automation.git
cd SauceDemo-automation
mvn clean install -DskipTests
```

## Running the Tests

**From the command line** (runs the TestNG suite and the reporting listener):


**From IntelliJ IDEA:** right-click `testng.xml` and choose **Run**.

> Running plain `mvn test` without the suite file runs tests by Surefire's naming convention and skips the `TestListener`, so no Extent report is generated.

## Reports

After a run, open the Extent report in your browser:

```
Reports/ExtentReport.html
```

`TestListener` logs each test's start, pass, fail (with the stack trace), and skip status to the report.

## Test Data

SauceDemo publishes public demo accounts on its login page. This project uses:

| Username        | Password       |
| --------------- | -------------- |
| `standard_user` | `secret_sauce` |

Expected product prices and the tax rate (8%) used for validation are in `utils/TestData.java`.

## Design Notes

- **Page Object Model:** each page has its own class holding locators and actions, which keeps tests readable.
- **Fresh browser per test:** `BaseTest` launches a new Edge instance before each test method and quits it afterwards.
- **Chained actions:** page methods return the page object, enabling calls like `inventory.addToCart(backpack).addToCart(bikeLight)`.
