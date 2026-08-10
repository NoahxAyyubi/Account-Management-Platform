package com.amp.mobile.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class SubscriptionPage extends BaseMobilePage {
    // Placeholder accessibility id. Replace after inspecting the AMP mobile app.
    private final By subscriptionHeader = AppiumBy.accessibilityId("subscription-header");

    public SubscriptionPage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isSubscriptionScreenVisible() {
        return isDisplayed(subscriptionHeader);
    }
}
