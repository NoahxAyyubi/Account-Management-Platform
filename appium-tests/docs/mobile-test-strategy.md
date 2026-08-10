# Mobile Test Strategy

This beginner-friendly strategy focuses on the core AMP mobile account flows first.

## Target Flows

- Login
- Dashboard
- View subscription
- View billing
- Update profile
- Logout

## Manual-First Approach

1. Explore manually.
2. Identify screens.
3. Inspect locators.
4. Create Page Objects.
5. Automate smoke flows.
6. Add regression coverage.

## Initial Smoke Coverage

Start with the shortest customer path that proves the app is usable:

- Launch app.
- Log in as a valid customer.
- Confirm dashboard appears.
- Open subscription details.
- Open billing details.
- Open profile.
- Log out.

## Regression Growth

After the smoke flow is stable, add negative and edge-case coverage:

- Invalid login.
- Empty username and password.
- Expired subscription state.
- Missing billing method.
- Profile validation errors.
- Network or server error messages.

Keep the first Appium tests small and stable. Mobile tests are more sensitive to emulator state, app loading time, and locator quality than API tests.
