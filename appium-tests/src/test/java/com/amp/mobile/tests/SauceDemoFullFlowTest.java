package com.amp.mobile.tests;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class SauceDemoFullFlowTest extends BaseMobileTest {
    private static final String APP_ID = "com.saucelabs.mydemoapp.android:id/";

    @Test(description = "Visible demo flow: catalog to completed checkout in Sauce Labs My Demo App.")
    public void customerCanCompleteCheckoutFlow() throws InterruptedException {
        pauseForDemo();

        waitForVisible(id("productRV"));
        tapFirstProduct();
        pauseForDemo();

        Assert.assertTrue(waitForVisible(text("Sauce Labs Backpack")).isDisplayed());
        scrollToResourceId("cartBt");
        tap(id("plusIV"));
        pauseForDemo();

        tap(id("cartBt"));
        pauseForDemo();

        tap(id("cartRL"));
        waitForVisible(text("My Cart"));
        scrollToText("Proceed To Checkout");
        tap(id("cartBt"));
        pauseForDemo();

        type(id("nameET"), "bod@example.com");
        type(id("passwordET"), "10203040");
        tap(id("loginBtn"));
        pauseForDemo();

        type(id("fullNameET"), "Rebecca Winter");
        type(id("address1ET"), "Mandorley 112");
        type(id("address2ET"), "Entrance 1");
        scrollToResourceId("cityET");
        type(id("cityET"), "Truro");
        type(id("stateET"), "Cornwall");
        type(id("zipET"), "89750");
        type(id("countryET"), "United Kingdom");
        scrollToText("To Payment");
        tap(id("paymentBtn"));
        pauseForDemo();

        type(id("nameET"), "Rebecca Winter");
        type(id("cardNumberET"), "3258125675687891");
        type(id("expirationDateET"), "03/25");
        type(id("securityCodeET"), "123");
        scrollToText("Review Order");
        tap(id("paymentBtn"));
        pauseForDemo();

        scrollToText("Place Order");
        tap(id("paymentBtn"));
        pauseForDemo();

        Assert.assertTrue(waitForVisible(text("Checkout Complete")).isDisplayed());
        pauseForDemo(5000);
    }

    private By id(String resourceId) {
        return AppiumBy.id(APP_ID + resourceId);
    }

    private By text(String value) {
        return AppiumBy.androidUIAutomator("new UiSelector().text(\"" + value + "\")");
    }

    private void tap(By locator) {
        waitForVisible(locator).click();
    }

    private void tapFirstProduct() {
        List<WebElement> productImages = driver.findElements(id("productIV"));
        Assert.assertFalse(productImages.isEmpty(), "At least one product card should be visible.");
        productImages.get(0).click();
    }

    private void type(By locator, String value) {
        WebElement element = waitForVisible(locator);
        element.clear();
        element.sendKeys(value);
        try {
            driver.hideKeyboard();
        } catch (Exception ignored) {
            // Keyboard may already be hidden depending on emulator state.
        }
    }

    private WebElement waitForVisible(By locator) {
        return new org.openqa.selenium.support.ui.WebDriverWait(
                driver,
                java.time.Duration.ofSeconds(config.getExplicitWaitSeconds()))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private void scrollToText(String value) {
        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView("
                        + "new UiSelector().text(\"" + value + "\"));"));
    }

    private void scrollToResourceId(String resourceId) {
        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView("
                        + "new UiSelector().resourceId(\"" + APP_ID + resourceId + "\"));"));
    }

    private void pauseForDemo() throws InterruptedException {
        pauseForDemo(1000);
    }

    private void pauseForDemo(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }
}
