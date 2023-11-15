package model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import db.MySQLDatabase;
import model.PrimaryKey;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Unlocking {
    @PrimaryKey
    public Integer socmed_id;
    public Integer dashboard_id;
    public String link_code;

    private static List<Unlocking> from(ResultSet resultSet) {
        try {
            Class<Unlocking> c = Unlocking.class;

            List<Unlocking> listOfUnlocking = new ArrayList<Unlocking>();
            while (resultSet.next()) {
                Unlocking instance = c.newInstance();
                for (Field field : c.getDeclaredFields()) {
                    field.setAccessible(true);
                    field.set(instance, resultSet.getObject(field.getName()));
                }
                listOfUnlocking.add(instance);
            }
            return listOfUnlocking;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Unlocking> findAll() {
        try {
            Class<Unlocking> c = Unlocking.class;

            String tableName = c.getSimpleName().toLowerCase();
            String query = "SELECT * FROM " + tableName;
            ResultSet resultSet = MySQLDatabase.getInstance().executeQuery(query);
            return from(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Unlocking findById(Integer id) {
        try {
            Class<Unlocking> c = Unlocking.class;

            String tableName = c.getSimpleName().toLowerCase();
            String query = "SELECT * FROM " + tableName + " WHERE `id` = " + id;
            ResultSet resultSet = MySQLDatabase.getInstance().executeQuery(query);
            return (Unlocking) from(resultSet).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Unlocking> findBy(String[] fields, String[] values) {
        try {
            Class<Unlocking> c = Unlocking.class;

            String tableName = c.getSimpleName().toLowerCase();
            String query = "SELECT * FROM " + tableName + " WHERE ";
            for (int i = 0; i < fields.length; i++) {
                query += "`" + fields[i] + "` = '" + values[i] + "'";
                if (i < fields.length - 1) {
                    query += " AND ";
                }
            }
            ResultSet resultSet = MySQLDatabase.getInstance().executeQuery(query);
            return from(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int insert(Unlocking instance) {
        try {
            Class<Unlocking> c = Unlocking.class;

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

    public static int update(Unlocking instance) {
        try {
            Class<Unlocking> c = Unlocking.class;

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

    public static int delete(Unlocking instance) {
        try {
            Class<Unlocking> c = Unlocking.class;

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
