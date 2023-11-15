package db;

import java.sql.Connection;
import java.sql.ResultSet;

public interface Database {
    public Connection getConnection();

    public ResultSet executeQuery(String query);

    public int executeUpdate(String query);
}
