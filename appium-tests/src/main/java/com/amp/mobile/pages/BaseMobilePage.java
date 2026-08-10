package com.amp.mobile.pages;

import com.amp.mobile.config.MobileConfig;
import com.amp.mobile.utils.WaitUtils;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

public class BaseMobilePage {
    protected final AndroidDriver driver;
    protected final WaitUtils waits;

    public BaseMobilePage(AndroidDriver driver) {
        this.driver = driver;
        this.waits = new WaitUtils(driver, new MobileConfig().getExplicitWaitSeconds());
    }

    protected void tap(By locator) {
        waits.waitForClickable(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement element = waits.waitForVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return waits.waitForVisible(locator).getText();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return waits.waitForVisible(locator).isDisplayed();
        } catch (NoSuchElementException | TimeoutException e) {
            return false;
        }
    }

    protected WebElement waitForVisible(By locator) {
        return waits.waitForVisible(locator);
    }
}
