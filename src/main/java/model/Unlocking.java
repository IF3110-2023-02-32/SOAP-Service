package model;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import db.MySQLDatabase;
import model.PrimaryKey;

public class Unlocking {
    @PrimaryKey
    public Integer socmed_id;
    public Integer dashboard_id;
    public String link_code;

    public Unlocking() {
        this.socmed_id = 0;
        this.dashboard_id = 0;
        this.link_code = "";
    }

    public Unlocking(Integer socmed_id, Integer dashboard_id, String link_code) {
        this.socmed_id = socmed_id;
        this.dashboard_id = dashboard_id;
        this.link_code = link_code;
    }

    public Unlocking(Integer socmed_id, String link_code) {
        this.socmed_id = socmed_id;
        this.link_code = link_code;
    }
    
    public Unlocking(String link_code, Integer dashboard_id) {
        this.dashboard_id = dashboard_id;
        this.link_code = link_code;
    }

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

    public static int insertUnlocked(Unlocking instance) {
        try {
            Class<Unlocking> c = Unlocking.class;

            String tableName = c.getSimpleName().toLowerCase();
            String query = "INSERT INTO " + tableName + " (";
            query += "socmed_id, link_code";
            query += ") VALUES (";
            query += "'" + instance.socmed_id + "', ";
            query += "'" + instance.link_code + "' ";
            query += ")";
            return MySQLDatabase.getInstance().executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
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

    public static int updateFromREST(Unlocking instance) {
        try {
            Class<Unlocking> c = Unlocking.class;

            String tableName = c.getSimpleName().toLowerCase();
            String query = "UPDATE " + tableName + " SET ";
            query += "dashboard_id = '" + instance.dashboard_id + "'";
            query += "`link_code` = " + instance.link_code;
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
