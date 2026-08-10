package com.amp.mobile.utils;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {
    private final AndroidDriver driver;
    private final Duration timeout;

    public WaitUtils(AndroidDriver driver, long timeoutSeconds) {
        this.driver = driver;
        this.timeout = Duration.ofSeconds(timeoutSeconds);
    }

    public WebElement waitForVisible(By locator) {
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickable(By locator) {
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }
}
