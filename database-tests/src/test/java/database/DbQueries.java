package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public final class DbQueries {
    private DbQueries() {
    }

    public static final String FIND_SUBSCRIPTION_BY_ID = """
            SELECT s.id, u.email, s.plan_type, s.status
            FROM subscriptions s
            JOIN users u ON u.id = s.user_id
            WHERE s.id = ?
            """;

    public static final String DELETE_SUBSCRIPTION_BY_ID = """
            DELETE FROM subscriptions
            WHERE id = ?
            """;

    public static SubscriptionRecord findSubscriptionById(Connection connection, String subscriptionId)
            throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(FIND_SUBSCRIPTION_BY_ID)) {
            statement.setObject(1, UUID.fromString(subscriptionId));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }

                return new SubscriptionRecord(
                        resultSet.getString("id"),
                        resultSet.getString("email"),
                        resultSet.getString("plan_type"),
                        resultSet.getString("status"));
            }
        }
    }

    public static int deleteSubscriptionById(Connection connection, String subscriptionId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_SUBSCRIPTION_BY_ID)) {
            statement.setObject(1, UUID.fromString(subscriptionId));
            return statement.executeUpdate();
        }
    }

    public record SubscriptionRecord(String subscriptionId, String userEmail, String planType, String status) {
    }
}
