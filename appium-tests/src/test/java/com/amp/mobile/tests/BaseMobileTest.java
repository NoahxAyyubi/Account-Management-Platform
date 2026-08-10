package com.amp.mobile.tests;

import com.amp.mobile.config.MobileConfig;
import com.amp.mobile.driver.DriverManager;
import io.appium.java_client.android.AndroidDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseMobileTest {
    protected AndroidDriver driver;
    protected MobileConfig config;

    @BeforeMethod
    public void setUp() {
        config = new MobileConfig();
        driver = new DriverManager(config).createAndroidDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
