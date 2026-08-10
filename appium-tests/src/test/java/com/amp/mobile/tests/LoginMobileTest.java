package com.amp.mobile.tests;

import com.amp.mobile.pages.DashboardPage;
import com.amp.mobile.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginMobileTest extends BaseMobileTest {

    @Test(enabled = false, description = "Scaffold only. Enable after adding a real APK and stable mobile locators.")
    public void validUserCanLoginAndSeeDashboard() {
        LoginPage loginPage = new LoginPage(driver);

        Assert.assertTrue(loginPage.isLoginScreenVisible(), "Login screen should be visible.");

        loginPage.enterUsername("valid.user@example.com");
        loginPage.enterPassword("Password123!");
        DashboardPage dashboardPage = loginPage.tapLogin();

        Assert.assertTrue(dashboardPage.isDashboardVisible(), "Dashboard should be visible after login.");
    }
}
