package db;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import utils.ConfigHandler;

public class MySQLDatabase implements Database {
    private static Database instance = null;
    private Connection con;

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

            this.con = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            System.out.println("connect to db failed");
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Successfully connected to database");
    }

    @Override
    public Connection getConnection() {
        System.out.println("Connection: " + this.con);
        return this.con;
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new MySQLDatabase();
        }

        return instance;
    }

    protected void finalize() throws SQLException {
        this.con.close();
    }

    @Override
    public ResultSet executeQuery(String query) {
        System.out.println(query);
        try {
            Statement statement = getConnection().createStatement();
            return statement.executeQuery(query + ";");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int executeUpdate(String query) {
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
