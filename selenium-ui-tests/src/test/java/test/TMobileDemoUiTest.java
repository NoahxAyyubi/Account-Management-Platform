package test;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class TMobileDemoUiTest extends BaseTest {
    private static final String BASE_URL = System.getProperty("demo.baseUrl", "http://localhost:3000");

    @Test(groups = {"smoke"})
    public void validLoginShouldLoadDashboard() {
        DemoPortalPage page = new DemoPortalPage(driver);

        page.open();
        page.login("qa.user@demo.com", "Password123!");

        Assert.assertTrue(page.isDashboardDisplayed(), "Dashboard should be displayed after login.");
        Assert.assertEquals(page.customerName(), "Demo Customer");
        Assert.assertEquals(page.accountStatus(), "ACTIVE");
    }

    @Test(groups = {"smoke"})
    public void subscriptionTableShouldShowSeededSubscription() {
        DemoPortalPage page = new DemoPortalPage(driver);

        page.open();
        page.login("qa.user@demo.com", "Password123!");

        Assert.assertTrue(page.subscriptionRowCount() > 0, "Subscription table should have rows.");
        Assert.assertEquals(page.planForSubscription("1001"), "Essentials");
        Assert.assertEquals(page.statusForSubscription("1001"), "ACTIVE");
    }

    @Test(groups = {"regression"})
    public void planChangeShouldUpdateDisplayedPlan() {
        DemoPortalPage page = new DemoPortalPage(driver);

        page.open();
        page.login("qa.user@demo.com", "Password123!");
        page.changePlan("Premium");

        Assert.assertEquals(page.planChangeMessage(), "Plan updated successfully");
        Assert.assertEquals(page.planForSubscription("1001"), "Premium");
        Assert.assertEquals(page.currentPlan(), "Premium");
    }

    private static class DemoPortalPage {
        private final WebDriver driver;
        private final WebDriverWait wait;

        private DemoPortalPage(WebDriver driver) {
            this.driver = driver;
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        }

        private void open() {
            driver.get(BASE_URL);
        }

        private void login(String email, String password) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).clear();
            driver.findElement(By.id("email")).sendKeys(email);
            driver.findElement(By.id("password")).clear();
            driver.findElement(By.id("password")).sendKeys(password);
            driver.findElement(By.id("loginButton")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dashboard")));
        }

        private boolean isDashboardDisplayed() {
            return driver.findElement(By.id("dashboard")).isDisplayed();
        }

        private String customerName() {
            return driver.findElement(By.id("customerName")).getText();
        }

        private String accountStatus() {
            return driver.findElement(By.id("accountStatus")).getText();
        }

        private String currentPlan() {
            return driver.findElement(By.id("currentPlan")).getText();
        }

        private int subscriptionRowCount() {
            return driver.findElements(By.cssSelector("#subscriptionTable tbody tr")).size();
        }

        private String planForSubscription(String subscriptionId) {
            return cellText(subscriptionId, 3);
        }

        private String statusForSubscription(String subscriptionId) {
            return cellText(subscriptionId, 4);
        }

        private String cellText(String subscriptionId, int columnNumber) {
            WebElement row = rowForSubscription(subscriptionId);
            return row.findElement(By.cssSelector("td:nth-child(" + columnNumber + ")")).getText();
        }

        private WebElement rowForSubscription(String subscriptionId) {
            List<WebElement> rows = driver.findElements(By.cssSelector("#subscriptionTable tbody tr"));

            for (WebElement row : rows) {
                if (row.getAttribute("data-subscription-id").equals(subscriptionId)) {
                    return row;
                }
            }

            throw new AssertionError("Could not find subscription row: " + subscriptionId);
        }

        private void changePlan(String plan) {
            new Select(driver.findElement(By.id("planSelect"))).selectByVisibleText(plan);
            driver.findElement(By.id("changePlanButton")).click();
            wait.until(ExpectedConditions.textToBe(By.id("planMessage"), "Plan updated successfully"));
        }

        private String planChangeMessage() {
            return driver.findElement(By.id("planMessage")).getText();
        }
    }
}
