package by.slava_borisov.hoteladmin.db;

import by.slava_borisov.hoteladmin.config.ConfigManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {

    private static final ConnectionManager INSTANCE = new ConnectionManager();
    private final ConfigManager config = ConfigManager.getInstance();
    private static final ThreadLocal<Connection> threadConnection = new ThreadLocal<>();

    private ConnectionManager() {
    }

    public static ConnectionManager getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        Connection conn = threadConnection.get();
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(
                    config.getDbUrl(),
                    config.getDbUser(),
                    config.getDbPassword()
            );
            threadConnection.set(conn);
        }
        return conn;
    }

    public void beginTransaction() throws SQLException {
        Connection conn = getConnection();
        conn.setAutoCommit(false);
    }

    public void commit() throws SQLException {
        Connection conn = threadConnection.get();
        if (conn != null) {
            conn.commit();
        }
    }

    public void rollback() {
        Connection conn = threadConnection.get();
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeThreadConnection() {
        Connection conn = threadConnection.get();
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                threadConnection.remove();
            }
        }
    }
}
