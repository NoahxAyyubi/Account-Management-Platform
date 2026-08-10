const { expect } = require('@playwright/test');

class DashboardPage {

  constructor(page) {
    this.page = page;

    this.regionDropdown = page.getByLabel('Region');

    this.applyFiltersButton =
      page.getByRole('button', { name: 'Apply Filters' });

    this.salesChart =
      page.locator('[data-testid="sales-chart"]');

    this.saveButton =
      page.getByRole('button', { name: 'Save Preferences' });
  }

  async goto() {
    await this.page.goto('http://localhost:3000/dashboard');
  }

  async selectRegion(region) {
    await this.regionDropdown.selectOption(region);
  }

  async applyFilters() {
    await this.applyFiltersButton.click();
  }

  async verifyChartVisible() {
    await expect(this.salesChart).toBeVisible();
  }

  async savePreferences() {
    await this.saveButton.click();
  }
}

module.exports = { DashboardPage };