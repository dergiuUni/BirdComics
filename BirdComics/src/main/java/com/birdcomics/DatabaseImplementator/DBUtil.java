package com.birdcomics.DatabaseImplementator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DBUtil {

    private static final int MAX_POOL_SIZE = 10;
    private static BlockingQueue<Connection> freeDbConnections;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            freeDbConnections = new ArrayBlockingQueue<>(MAX_POOL_SIZE);
            initializeConnections();
        } catch (ClassNotFoundException e) {
            System.out.println("DB driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error initializing database connections: " + e.getMessage());
        }
    }

    private static void initializeConnections() throws SQLException {
        for (int i = 0; i < MAX_POOL_SIZE; i++) {
            Connection connection = createDBConnection();
            freeDbConnections.offer(connection);
        }
    }

    public static Connection createDBConnection() throws SQLException {
        String ip = "localhost";
        String port = "3306";
        String db = "BirdComics";
        String username = "root";
        String password = "root";

        return DriverManager.getConnection(
            "jdbc:mysql://" + ip + ":" + port + "/" + db +
            "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", 
            username, 
            password
        );
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = freeDbConnections.poll();
        if (connection == null || connection.isClosed()) {
            connection = createDBConnection();
        }
        return connection;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed() && !freeDbConnections.offer(connection)) {
                    // Queue full, close the connection
                    connection.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error closing connection: " + ex.getMessage());
            }
        }
    }

    public static void closeConnection(PreparedStatement ps) {
        try {
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection(ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}