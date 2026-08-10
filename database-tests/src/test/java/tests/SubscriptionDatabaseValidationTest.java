package tests;

import api.SubscriptionApiClient;
import database.DatabaseManager;
import database.DbAssertions;
import database.DbQueries;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

@Tag("database")
class SubscriptionDatabaseValidationTest {
    private static final String API_BASE_URL = System.getProperty("api.baseUrl", "http://localhost:3000");
    private static final String TEST_USER_EMAIL = "bug_user@test.com";
    private static final String PLAN_TYPE = "PREMIUM";

    private String createdSubscriptionId;

    @AfterEach
    void cleanUpCreatedSubscription() throws SQLException {
        if (createdSubscriptionId == null || !DatabaseManager.isConfigured()) {
            return;
        }

        try (Connection connection = DatabaseManager.getConnection()) {
            DbQueries.deleteSubscriptionById(connection, createdSubscriptionId);
        }
    }

    @AfterAll
    static void closeDatabasePool() {
        DatabaseManager.closeDataSource();
    }

    @Test
    void shouldValidateSubscriptionCreatedByApiFlow() throws SQLException {
        Assumptions.assumeTrue(
                DatabaseManager.isConfigured(),
                "Fill local DB config before running database validation tests.");

        SubscriptionApiClient apiClient = new SubscriptionApiClient(API_BASE_URL);
        SubscriptionApiClient.CreateSubscriptionResponse apiResponse =
                apiClient.createSubscription(TEST_USER_EMAIL, PLAN_TYPE);

        Assertions.assertEquals(201, apiResponse.statusCode(), "API should create a subscription.");

        String subscriptionId = JsonPath.from(apiResponse.body()).getString("subscriptionId");
        String apiStatus = JsonPath.from(apiResponse.body()).getString("status");
        Assertions.assertNotNull(subscriptionId, "API response should include subscriptionId.");
        createdSubscriptionId = subscriptionId;

        try (Connection connection = DatabaseManager.getConnection()) {
            DbQueries.SubscriptionRecord subscription = DbQueries.findSubscriptionById(connection, subscriptionId);

            DbQueries.SubscriptionRecord savedSubscription =
                    DbAssertions.assertSubscriptionExists(subscription, TEST_USER_EMAIL);

            DbAssertions.assertSubscriptionHasPlan(savedSubscription, PLAN_TYPE);
            DbAssertions.assertSubscriptionStatus(savedSubscription, apiStatus);
        }
    }
}
