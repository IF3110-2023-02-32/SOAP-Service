package db;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import utils.ConfigHandler;

public class MySQLDatabase {
    private static Connection connection;

    private static final String DB_URL_KEY = "db.url";
    private static final String DB_USER_KEY = "db.user";
    private static final String DB_PASS_KEY = "db.pass";

    private MySQLDatabase() {
        try {
            ConfigHandler ch = ConfigHandler.getInstance();
            String url = ch.get(DB_URL_KEY);
            String user = ch.get(DB_USER_KEY);
            String pass = ch.get(DB_PASS_KEY);
            System.out.println("Trying to connect to database at " + url + " with user " + user + " and pass " + pass);

            connection = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                ConfigHandler ch = ConfigHandler.getInstance();
                String url = ch.get(DB_URL_KEY);
                String user = ch.get(DB_USER_KEY);
                String pass = ch.get(DB_PASS_KEY);
                System.out.println(
                        "Trying to connect to database at " + url + " with user " + user + " and pass " + pass);
                connection = DriverManager.getConnection(url, user, pass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Connection: " + connection);
        return connection;
    }

    protected void finalize() throws SQLException {
        connection.close();
    }

    public static ResultSet executeQuery(String query) {
        System.out.println(query);
        try {
            Statement statement = getConnection().createStatement();
            return statement.executeQuery(query + ";");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int executeUpdate(String query) {
        System.out.println(query);
        try {
            Statement statement = getConnection().createStatement();
            return statement.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
