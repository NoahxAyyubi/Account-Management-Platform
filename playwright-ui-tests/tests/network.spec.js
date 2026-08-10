const { test } = require('@playwright/test');

test('mock api failure', async ({ page }) => {

    // observe outgoing requests
    page.on('request', request => {

        if (request.url().includes('/users')) {

            console.log('REQUEST:', request.url());

        }

    });

    // observe incoming responses
    page.on('response', response => {

        if (response.url().includes('/users')) {

            console.log('RESPONSE STATUS:', response.status());

        }

    });

    // intercept api request
    await page.route('**/users**', async route => {

        console.log('INTERCEPTED USERS API');

        // fake backend failure
        await route.fulfill({
            status: 500,
            contentType: 'application/json',
            body: JSON.stringify({
                error: 'Server crashed'
            })
        });

    });

    // open website
    await page.goto('https://reqres.in');

    // trigger api request
    await page.evaluate(async () => {

        const response =
            await fetch('https://reqres.in/api/users?page=2');

        console.log('FETCH STATUS:', response.status);

    });

});