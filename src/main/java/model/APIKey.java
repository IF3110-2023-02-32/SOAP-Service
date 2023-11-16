package model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Field;

import db.MySQLDatabase;
import model.PrimaryKey;

public class APIKey {
    @PrimaryKey
    public Integer id;
    public String key;
    public Timestamp timestamp;

    public APIKey() {
        this.id = 0;
        this.key = "";
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public APIKey(Integer id, String key, Timestamp timestamp) {
        this.id = id;
        this.key = key;
        this.timestamp = timestamp;
    }

    private static List<APIKey> from(ResultSet resultSet) {
        try {
            Class<APIKey> c = APIKey.class;

            List<APIKey> listOfAPIKey = new ArrayList<APIKey>();
            while (resultSet.next()) {
                APIKey instance = c.newInstance();
                for (Field field : c.getDeclaredFields()) {
                    field.setAccessible(true);
                    field.set(instance, resultSet.getObject(field.getName()));
                }
                listOfAPIKey.add(instance);
            }
            return listOfAPIKey;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<APIKey> findAll() {
        try {
            Class<APIKey> c = APIKey.class;

            String tableName = c.getSimpleName().toLowerCase();
            String query = "SELECT * FROM " + tableName;
            ResultSet resultSet = MySQLDatabase.getInstance().executeQuery(query);
            return from(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static APIKey findById(Integer id) {
        try {
            Class<APIKey> c = APIKey.class;

            String tableName = c.getSimpleName().toLowerCase();
            String query = "SELECT * FROM " + tableName + " WHERE `id` = " + id;
            ResultSet resultSet = MySQLDatabase.getInstance().executeQuery(query);
            return (APIKey) from(resultSet).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<APIKey> findBy(String field, String value) {
        try {
            Class<APIKey> c = APIKey.class;

            String tableName = c.getSimpleName().toLowerCase();
            String query = "SELECT * FROM " + tableName + " WHERE `" + field + "` = '" + value + "'";
            ResultSet resultSet = MySQLDatabase.getInstance().executeQuery(query);
            return from(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int insert(APIKey instance) {
        try {
            Class<APIKey> c = APIKey.class;

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

    public static int update(APIKey instance) {
        try {
            Class<APIKey> c = APIKey.class;

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

    public static int delete(APIKey instance) {
        try {
            Class<APIKey> c = APIKey.class;

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
