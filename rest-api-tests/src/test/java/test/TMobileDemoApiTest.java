package test;

import io.restassured.response.Response;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TMobileDemoApiTest {
    private static final String BASE_URL = System.getProperty("demo.baseUrl", "http://localhost:3000");

    private final DemoApiClient apiClient = new DemoApiClient(BASE_URL);

    @Test
    @Tag("smoke")
    void validLoginShouldReturnTokenAndUser() {
        Response response = apiClient.login("qa.user@demo.com", "Password123!");

        assertEquals(200, response.statusCode());
        assertEquals(true, response.jsonPath().getBoolean("authenticated"));
        assertNotNull(response.jsonPath().getString("token"));
        assertEquals("Demo Customer", response.jsonPath().getString("user.name"));
    }

    @Test
    @Tag("smoke")
    void retrieveExistingSubscriptionShouldReturnSeededAccount() {
        Response response = apiClient.getSubscription(1001);

        assertEquals(200, response.statusCode());
        assertEquals(1001, response.jsonPath().getInt("id"));
        assertEquals("Essentials", response.jsonPath().getString("plan"));
        assertEquals("ACTIVE", response.jsonPath().getString("status"));
        assertFalse(response.jsonPath().getString("customerName").isBlank());
    }

    @Test
    @Tag("regression")
    void changeSubscriptionPlanShouldPersistForTestFlow() {
        apiClient.resetDemoData();

        try {
            Response updateResponse = apiClient.changePlan(1001, "Premium");

            assertEquals(200, updateResponse.statusCode());
            assertEquals("Premium", updateResponse.jsonPath().getString("subscription.plan"));

            Response getResponse = apiClient.getSubscription(1001);

            assertEquals(200, getResponse.statusCode());
            assertEquals("Premium", getResponse.jsonPath().getString("plan"));
        } finally {
            apiClient.resetDemoData();
        }
    }

    private static class DemoApiClient {
        private final String baseUrl;

        private DemoApiClient(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        private Response login(String email, String password) {
            return given()
                    .baseUri(baseUrl)
                    .contentType("application/json")
                    .body(Map.of("email", email, "password", password))
                    .when()
                    .post("/api/auth/login");
        }

        private Response getSubscription(int subscriptionId) {
            return given()
                    .baseUri(baseUrl)
                    .accept("application/json")
                    .when()
                    .get("/api/subscriptions/" + subscriptionId);
        }

        private Response changePlan(int subscriptionId, String plan) {
            return given()
                    .baseUri(baseUrl)
                    .contentType("application/json")
                    .body(Map.of("plan", plan))
                    .when()
                    .put("/api/subscriptions/" + subscriptionId + "/plan");
        }

        private Response resetDemoData() {
            return given()
                    .baseUri(baseUrl)
                    .when()
                    .post("/api/test/reset");
        }
    }
}
