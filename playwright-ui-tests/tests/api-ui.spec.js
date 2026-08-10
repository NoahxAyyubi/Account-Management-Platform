const { test, expect } = require('@playwright/test');

test('report created through API appears in UI',
async ({ page, request }) => {

  const response = await request.post('/api/reports', {
    data: {
      name: 'Revenue Report',
      status: 'READY'
    }
  });

  expect(response.ok()).toBeTruthy();

  await page.goto('/dashboard');

  await expect(
    page.getByText('Revenue Report')
  ).toBeVisible();
});