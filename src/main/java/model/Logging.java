package model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Field;

import db.MySQLDatabase;
import model.PrimaryKey;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Logging {
    @PrimaryKey
    public Integer id;
    public String description;
    public String ip;
    public String endpoint;
    public Timestamp timestamp;

    private static List<Logging> from(ResultSet resultSet) {
        try {
            Class<Logging> c = Logging.class;

            List<Logging> listOfLogging = new ArrayList<Logging>();
            while (resultSet.next()) {
                Logging instance = c.newInstance();
                for (Field field : c.getDeclaredFields()) {
                    field.setAccessible(true);
                    field.set(instance, resultSet.getObject(field.getName()));
                }
                listOfLogging.add(instance);
            }
            return listOfLogging;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Logging> findAll() {
        try {
            Class<Logging> c = Logging.class;

            String tableName = c.getSimpleName().toLowerCase();
            String query = "SELECT * FROM " + tableName;
            ResultSet resultSet = MySQLDatabase.getInstance().executeQuery(query);
            return from(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Logging findById(Integer id) {
        try {
            Class<Logging> c = Logging.class;

            String tableName = c.getSimpleName().toLowerCase();
            String query = "SELECT * FROM " + tableName + " WHERE `id` = " + id;
            ResultSet resultSet = MySQLDatabase.getInstance().executeQuery(query);
            return (Logging) from(resultSet).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Logging> findBy(String field, String value) {
        try {
            Class<Logging> c = Logging.class;

            String tableName = c.getSimpleName().toLowerCase();
            String query = "SELECT * FROM " + tableName + " WHERE `" + field + "` = '" + value + "'";
            ResultSet resultSet = MySQLDatabase.getInstance().executeQuery(query);
            return from(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int insert(Logging instance) {
        try {
            Class<Logging> c = Logging.class;

            String tableName = c.getSimpleName().toLowerCase();
            String query = "INSERT INTO " + tableName + " (";
            for (Field field : c.getDeclaredFields()) {
                field.setAccessible(true);
                if (!field.getName().equals("id")) {
                    query += field.getName() + ", ";
                }
            }
            query = query.substring(0, query.length() - 2) + ") VALUES (";
            for (Field field : c.getDeclaredFields()) {
                field.setAccessible(true);
                if (!field.getName().equals("id")) {
                    query += "'" + field.get(instance) + "', ";
                }
            }
            query = query.substring(0, query.length() - 2) + ")";
            return MySQLDatabase.getInstance().executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int update(Logging instance) {
        try {
            Class<Logging> c = Logging.class;

            String tableName = c.getSimpleName().toLowerCase();
            String query = "UPDATE " + tableName + " SET ";
            for (Field field : c.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(PrimaryKey.class)) {
                    continue;
                }
                query += field.getName() + " = '" + field.get(instance) + "', ";
            }
            query = query.substring(0, query.length() - 2);
            boolean flag = false;
            for (Field field : c.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(PrimaryKey.class)) {
                    if (flag) {
                        query += " AND ";
                    } else {
                        query += " WHERE ";
                        flag = true;
                    }
                    query += "`" + field.getName() + "` = '" + field.get(instance) + "'";
                }
            }
            return MySQLDatabase.getInstance().executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int delete(Logging instance) {
        try {
            Class<Logging> c = Logging.class;

            String tableName = c.getSimpleName().toLowerCase();
            String query = "DELETE FROM " + tableName + " WHERE ";
            boolean flag = false;
            for (Field field : c.getDeclaredFields()) {
                if (flag) {
                    query += " AND ";
                } else {
                    flag = true;
                }
                field.setAccessible(true);
                if (field.isAnnotationPresent(PrimaryKey.class)) {
                    query += "`" + field.getName() + "` = '" + field.get(instance) + "'";
                }
            }
            return MySQLDatabase.getInstance().executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
