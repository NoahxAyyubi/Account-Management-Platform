# Appium Architecture

The AMP mobile automation flow looks like this:

```text
Java Test
↓
Appium Java Client
↓
Appium Server
↓
UiAutomator2 Driver
↓
Android Emulator / Real Device
↓
Mobile App
```

## What Each Layer Does

- Java Test: TestNG test classes that describe the scenario.
- Appium Java Client: Java library used by the tests to send mobile automation commands.
- Appium Server: Local server that receives commands from the client.
- UiAutomator2 Driver: Android-specific Appium driver that translates commands for Android.
- Android Emulator / Real Device: The target device where the app runs.
- Mobile App: The AMP APK under test.

## Appium Compared To Selenium

Selenium controls browsers. It is used for web UI automation with browser drivers such as ChromeDriver.

Appium controls mobile apps through platform drivers. For Android, this project uses UiAutomator2. For iOS, Appium usually uses XCUITest.

The Page Object Model stays familiar across both tools, but the locators and device setup are mobile-specific.
