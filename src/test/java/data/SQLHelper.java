package data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    private SQLHelper() {
    }

    private static Connection getConn() throws SQLException {
        String dbUrl = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPass = System.getenv("DB_PASS");
        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }

    @SneakyThrows
    public static String getPaymentStatus() {
        var connection = getConn();
        String query = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        String statusPayment = QUERY_RUNNER.query(connection, query, new ScalarHandler<>());
        return statusPayment;
    }

    @SneakyThrows
    public static boolean isOrderSuccessful() {
        var connection = getConn();
        String query = "SELECT payment_id FROM order_entity ORDER BY created DESC LIMIT 1";
        String orderId = QUERY_RUNNER.query(connection, query, new ScalarHandler<>());
        boolean isSuccessful = false;
        if (orderId != null) {
            isSuccessful = true;
        }
        return isSuccessful;
    }

    @SneakyThrows
    public static void cleanupDB() {
        var connection = getConn();
        QUERY_RUNNER.execute(connection, "DELETE FROM order_entity");
        QUERY_RUNNER.execute(connection, "DELETE FROM payment_entity");
        QUERY_RUNNER.execute(connection, "DELETE FROM credit_request_entity");
    }
}