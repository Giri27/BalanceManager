package BalanceManager.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Francesco Girardi
 * @version 1.0.0
 * @since 1.0.0
 */
public class DatabaseConnection {

    private Connection connection;
    private Statement statement;

    private String URL = "jdbc:postgresql://localhost:5432/balance_manager";

    /**
     * Create the Database Connection and Statement
     */
    public DatabaseConnection() {
        setConnection();
    }

    /**
     * Get Database Connection
     *
     * @return Connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Get Database Statement
     *
     * @return Statement
     */
    public Statement getStatement() {
        return statement;
    }

    private void setConnection() {
        try {
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection(URL, "postgres", "Rosita98!");

            setStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void setStatement() {
        try {
            statement = connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
