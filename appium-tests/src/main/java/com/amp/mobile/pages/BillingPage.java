package com.amp.mobile.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class BillingPage extends BaseMobilePage {
    // Placeholder resource-id. Replace with the real billing screen locator from Appium Inspector.
    private final By billingHeader = AppiumBy.id("com.amp.mobile:id/billingHeader");

    public BillingPage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isBillingScreenVisible() {
        return isDisplayed(billingHeader);
    }
}
