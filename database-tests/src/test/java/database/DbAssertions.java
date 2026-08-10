package database;

import org.junit.jupiter.api.Assertions;

public final class DbAssertions {
    private DbAssertions() {
    }

    public static DbQueries.SubscriptionRecord assertSubscriptionExists(
            DbQueries.SubscriptionRecord subscription,
            String userEmail) {
        Assertions.assertNotNull(subscription, "Expected subscription row to exist for user email: " + userEmail);

        return subscription;
    }

    public static void assertSubscriptionHasPlan(DbQueries.SubscriptionRecord subscription, String expectedPlanName) {
        Assertions.assertEquals(
                expectedPlanName,
                subscription.planType(),
                "Subscription plan should match database value.");
    }

    public static void assertSubscriptionStatus(DbQueries.SubscriptionRecord subscription, String expectedStatus) {
        Assertions.assertEquals(
                expectedStatus,
                subscription.status(),
                "Subscription status should match database value.");
    }
}
