package api;

import static io.restassured.RestAssured.given;

public class SubscriptionApiClient {
    private final String baseUrl;

    public SubscriptionApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public CreateSubscriptionResponse createSubscription(String userEmail, String planType) {
        String requestBody = """
                {
                  "userEmail": "%s",
                  "planType": "%s"
                }
                """.formatted(userEmail, planType);

        var response = given()
                .baseUri(baseUrl)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/subscriptions");

        return new CreateSubscriptionResponse(
                response.statusCode(),
                response.asString()
        );
    }

    public CreateSubscriptionResponse getSubscription(String subscriptionId) {
        var response = given()
                .baseUri(baseUrl)
                .accept("application/json")
                .when()
                .get("/api/subscriptions/" + subscriptionId);

        return new CreateSubscriptionResponse(
                response.statusCode(),
                response.asString()
        );
    }

    public record CreateSubscriptionResponse(int statusCode, String body) {
    }
}
