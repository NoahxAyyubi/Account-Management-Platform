package com.amp.mobile.tests;

import com.amp.mobile.config.MobileConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SmokeMobileTest {

    @Test(description = "Runs without an APK to prove the scaffold can load mobile configuration.")
    public void frameworkConfigurationLoads() {
        MobileConfig config = new MobileConfig();

        Assert.assertEquals(config.getPlatformName(), "Android");
        Assert.assertEquals(config.getAutomationName(), "UiAutomator2");
        Assert.assertFalse(config.getAppiumServerUrl().isBlank(), "Appium server URL should be configured.");
    }

    @Test(enabled = false, description = "Scaffold only. Enable after emulator, Appium server, and APK are ready.")
    public void mobileDriverCanStart() {
        // Extend BaseMobileTest or create a DriverManager here once the real app details are available.
    }
}
