package com.amp.mobile.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MobileConfig {
    private static final String CONFIG_FILE = "config.properties";
    private final Properties properties = new Properties();

    public MobileConfig() {
        loadProperties();
    }

    private void loadProperties() {
        try (InputStream classpathStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (classpathStream != null) {
                properties.load(classpathStream);
                return;
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load mobile config from classpath.", e);
        }

        try (InputStream fileStream = new FileInputStream("src/test/resources/" + CONFIG_FILE)) {
            properties.load(fileStream);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load src/test/resources/" + CONFIG_FILE, e);
        }
    }

    public String getPlatformName() {
        return get("platformName");
    }

    public String getAutomationName() {
        return get("automationName");
    }

    public String getDeviceName() {
        return get("deviceName");
    }

    public String getAppiumServerUrl() {
        return get("appiumServerUrl");
    }

    public String getAppPath() {
        return get("appPath");
    }

    public String getAppPackage() {
        return get("appPackage");
    }

    public String getAppActivity() {
        return get("appActivity");
    }

    public long getImplicitWaitSeconds() {
        return Long.parseLong(get("implicitWaitSeconds"));
    }

    public long getExplicitWaitSeconds() {
        return Long.parseLong(get("explicitWaitSeconds"));
    }

    private String get(String key) {
        return properties.getProperty(key, "").trim();
    }
}
