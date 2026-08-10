package com.amp.mobile.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class DashboardPage extends BaseMobilePage {
    // Placeholder locators. Capture real resource-id or accessibility id values with Appium Inspector.
    private final By dashboardTitle = AppiumBy.id("com.amp.mobile:id/dashboardTitle");
    private final By billingTab = AppiumBy.accessibilityId("billing-tab");
    private final By subscriptionTab = AppiumBy.accessibilityId("subscription-tab");
    private final By profileTab = AppiumBy.accessibilityId("profile-tab");

    public DashboardPage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isDashboardVisible() {
        return isDisplayed(dashboardTitle);
    }

    public BillingPage openBilling() {
        tap(billingTab);
        return new BillingPage(driver);
    }

    public SubscriptionPage openSubscription() {
        tap(subscriptionTab);
        return new SubscriptionPage(driver);
    }

    public ProfilePage openProfile() {
        tap(profileTab);
        return new ProfilePage(driver);
    }
}
