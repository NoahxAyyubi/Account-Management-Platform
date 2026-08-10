package com.amp.mobile.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class LoginPage extends BaseMobilePage {
    // Replace placeholder locators with values from Appium Inspector.
    // Prefer resource-id and accessibility id. Use class name when stable. Use XPath only as a last resort.
    private final By usernameInput = AppiumBy.accessibilityId("login-username");
    private final By passwordInput = AppiumBy.accessibilityId("login-password");
    private final By loginButton = AppiumBy.accessibilityId("login-submit");
    private final By loginTitle = AppiumBy.id("com.amp.mobile:id/loginTitle");

    public LoginPage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isLoginScreenVisible() {
        return isDisplayed(loginTitle);
    }

    public void enterUsername(String username) {
        type(usernameInput, username);
    }

    public void enterPassword(String password) {
        type(passwordInput, password);
    }

    public DashboardPage tapLogin() {
        tap(loginButton);
        return new DashboardPage(driver);
    }
}
