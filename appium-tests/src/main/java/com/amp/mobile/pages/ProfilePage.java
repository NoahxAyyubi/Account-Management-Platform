package com.amp.mobile.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class ProfilePage extends BaseMobilePage {
    // Placeholder class name. Prefer a stable resource-id or accessibility id when available.
    private final By profileHeader = AppiumBy.className("android.widget.TextView");

    public ProfilePage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isProfileScreenVisible() {
        return isDisplayed(profileHeader);
    }
}
