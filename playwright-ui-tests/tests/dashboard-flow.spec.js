const { test, expect } = require('@playwright/test');

const { DashboardPage } =
  require('../pages/DashboardPage');

test('user can filter dashboard data', async ({ page }) => {

  const dashboard = new DashboardPage(page);

  await dashboard.goto();

  await dashboard.selectRegion('North America');

  await dashboard.applyFilters();

  await dashboard.verifyChartVisible();

  await dashboard.savePreferences();

  await expect(
    page.getByText('Preferences Saved')
  ).toBeVisible();
});