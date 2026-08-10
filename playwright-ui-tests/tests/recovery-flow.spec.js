const { test, expect } = require('@playwright/test');

test('dashboard retries after API failure', async ({ page }) => {

  let firstCall = true;

  await page.route('**/api/analytics', async route => {

    if (firstCall) {

      firstCall = false;

      await route.fulfill({
        status: 500,
        body: JSON.stringify({
          error: 'temporary failure'
        })
      });

    } else {

      await route.fulfill({
        status: 200,
        body: JSON.stringify({
          revenue: 120000
        })
      });
    }
  });

  await page.goto('http://localhost:3000/dashboard');

  await expect(
    page.getByText('Retrying...')
  ).toBeVisible();

  await expect(
    page.getByText('120000')
  ).toBeVisible();
});