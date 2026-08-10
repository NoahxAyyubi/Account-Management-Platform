# AMP Appium Mobile Tests

This module is the starting point for Android mobile automation in the Account Management Platform SDET portfolio project. It keeps mobile automation separate from the existing API, Selenium UI, Playwright, database, and CI practice areas.

The current framework is intentionally a scaffold. It can compile and run its configuration smoke test, and it now includes the Sauce Labs My Demo App APK as a practice Android app. The demo login flow is disabled until stable mobile locators are added.

## Tech Stack

- Java
- Maven
- TestNG
- Appium Java Client
- Selenium Java
- SLF4J simple logger
- Android emulator with UiAutomator2

## Required Installs

- Java
- Maven
- Node.js
- Appium
- Appium UiAutomator2 driver
- Android Studio
- Android SDK
- Android Emulator
- Appium Inspector

## Install Appium

```bash
npm install -g appium
appium driver install uiautomator2
appium --version
appium driver list --installed
```

## Start Appium Server

```bash
npm run appium
```

By default, the framework expects the server at:

```text
http://127.0.0.1:4723
```

## Run Tests

From the `appium-tests` folder:

```bash
npm test
```

The enabled smoke test only validates framework configuration. Real emulator tests can be enabled after the Appium server and Android emulator are running.

## Start Android Emulator

This project has an Android 34 Pixel emulator profile named:

```text
AMP_Pixel_API_34
```

From the `appium-tests` folder:

```bash
npm run emulator:list
npm run emulator:start
npm run adb:devices
```

Keep the `npm run emulator:start` terminal open while using the emulator. Open a second terminal for Appium and test commands.

## APK and App Details

The default config points to the Sauce Labs My Demo App APK:

```text
src/test/resources/apps/my-demo-app.apk
```

The APK is stored inside the test resources app folder so Appium can install it on the Android emulator.

If you later switch to an installed AMP app instead of an APK path, add the installed app details:

```properties
appPackage=com.example.amp
appActivity=.MainActivity
```

Real locator values should come from Appium Inspector. Prefer `resource-id` and accessibility ids. Use class names only when stable, and XPath only as a last resort.
