package com.birdcomics.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DBUtil {

    private static final int MAX_POOL_SIZE = 20;
    private BlockingQueue<Connection> freeDbConnections;

    // Istanza singleton
    private static DBUtil instance;

    // Costruttore privato
    private DBUtil() {
        try {
            // Carica il driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Inizializza la coda di connessioni
            freeDbConnections = new ArrayBlockingQueue<>(MAX_POOL_SIZE);
            initializeConnections();
        } catch (ClassNotFoundException e) {
            System.out.println("DB driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error initializing database connections: " + e.getMessage());
        }
    }
    
    /*
    public class DBUtil {
        private static volatile DBUtil instance; // Volatile per garantire visibilità nei thread
        private DBUtil() {   }

        public static DBUtil getInstance() {
            if (instance == null) { // Primo check senza lock
                synchronized (DBUtil.class) {
                    if (instance == null) { // Secondo check con lock
                        instance = new DBUtil();
                    }
                }
            }
            return instance;
        }
    }
    */

    // Metodo pubblico per ottenere l'istanza singleton
    public static synchronized DBUtil getInstance() {
        if (instance == null) {
            instance = new DBUtil();
        }
        return instance;
    }

    private void initializeConnections() throws SQLException {
        for (int i = 0; i < MAX_POOL_SIZE; i++) {
            Connection connection = createDBConnection();
            freeDbConnections.offer(connection);
        }
    }

    public Connection createDBConnection() throws SQLException {
        String ip = "localhost";
        String port = "3306";
        String db = "BirdComics";
        String email = "root";
        String password = "root";

        return DriverManager.getConnection(
            "jdbc:mysql://" + ip + ":" + port + "/" + db +
            "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", 
            email, 
            password
        );
    }

    public Connection getConnection() throws SQLException {
        Connection connection = freeDbConnections.poll();
        if (connection == null || connection.isClosed()) {
            connection = createDBConnection();
        }
        return connection;
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed() && !freeDbConnections.offer(connection)) {
                    // Se la coda è piena, chiudi la connessione
                    connection.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error closing connection: " + ex.getMessage());
            }
        }
    }

    public void closeConnection(PreparedStatement ps) {
        try {
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}