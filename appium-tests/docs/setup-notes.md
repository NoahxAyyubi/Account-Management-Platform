# Setup Notes

Use this checklist when preparing the local Android Appium environment.

- Install Android Studio.
- Install Android SDK.
- Create an Android emulator.
- Start the emulator.
- Install Node.js.
- Install Appium with `npm install -g appium`.
- Install the UiAutomator2 driver with `appium driver install uiautomator2`.
- Start the Appium server with `appium`.
- Open Appium Inspector.
- Connect Appium Inspector to the emulator.
- Inspect mobile elements.
- Record stable `resource-id` and accessibility id locators.
- Add the real AMP APK later under `appium-tests/apps/` or update `appPath`.
- Add `appPackage` and `appActivity` later if testing an already-installed app.
