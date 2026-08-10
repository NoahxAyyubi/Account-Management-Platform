package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import api.SubscriptionApiClient;

@Tag("api")
class SubscriptionApiStatusTest {
    private static final String API_BASE_URL = System.getProperty("api.baseUrl", "http://localhost:3000");
    private static final String TEST_USER_EMAIL = "bug_user@test.com";
    private static final String PLAN_TYPE = "PREMIUM";
    private static final int EXPECTED_CREATED_STATUS = 201;

    @Test
    void shouldReturnCreatedWhenCreatingSubscription() {
        SubscriptionApiClient apiClient = new SubscriptionApiClient(API_BASE_URL);

        SubscriptionApiClient.CreateSubscriptionResponse response =
                apiClient.createSubscription(TEST_USER_EMAIL, PLAN_TYPE);

        assertEquals(EXPECTED_CREATED_STATUS, response.statusCode(), "Create subscription API should succeed.");
        assertFalse(response.body().isBlank(), "Create subscription API should return a response body.");

        JsonPath responseJson = JsonPath.from(response.body());
        assertNotNull(responseJson.getString("subscriptionId"), "API response should include subscriptionId.");
        assertEquals(TEST_USER_EMAIL, responseJson.getString("userEmail"), "API response should include user email.");
        assertEquals(PLAN_TYPE, responseJson.getString("planType"), "API response should include plan type.");
        assertEquals("ACTIVE", responseJson.getString("status"), "API response should show active status.");
    }
}
