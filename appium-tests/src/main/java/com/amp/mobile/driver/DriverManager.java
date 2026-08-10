package com.amp.mobile.driver;

import com.amp.mobile.config.MobileConfig;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

public class DriverManager {
    private final MobileConfig config;

    public DriverManager(MobileConfig config) {
        this.config = config;
    }

    public AndroidDriver createAndroidDriver() {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(config.getPlatformName())
                .setAutomationName(config.getAutomationName())
                .setDeviceName(config.getDeviceName());

        configureAppUnderTest(options);

        AndroidDriver driver = new AndroidDriver(getAppiumServerUrl(), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWaitSeconds()));
        return driver;
    }

    private void configureAppUnderTest(UiAutomator2Options options) {
        String appPath = config.getAppPath();
        String appPackage = config.getAppPackage();
        String appActivity = config.getAppActivity();

        if (!appPath.isBlank()) {
            File appFile = new File(appPath);
            if (appFile.exists()) {
                options.setApp(appFile.getAbsolutePath());
            } else {
                // Add the real APK later at appium-tests/apps/amp-demo.apk or update appPath.
                // Leaving the app capability unset keeps the framework compiling before an APK exists.
            }
        }

        if (!appPackage.isBlank()) {
            options.setAppPackage(appPackage);
        }

        if (!appActivity.isBlank()) {
            options.setAppActivity(appActivity);
        }
    }

    private URL getAppiumServerUrl() {
        try {
            return URI.create(config.getAppiumServerUrl()).toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid Appium server URL: " + config.getAppiumServerUrl(), e);
        }
    }
}
