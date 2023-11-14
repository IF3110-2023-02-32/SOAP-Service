package model;

import java.lang.reflect.Field;
import java.lang.reflect.Constructor;

public abstract class BaseModel {
    protected BaseModel() {}
    public static <T extends BaseModel> T constructFromSQL(java.sql.ResultSet rs, Class<T> objClass) throws SQLObjectConstructException {
        try {
            Constructor<T> objConstructor = objClass.getConstructor();
            objConstructor.setAccessible(true);
            T instance = objConstructor.newInstance();
            
            for (Field field : objClass.getDeclaredFields()) {
                field.setAccessible(true);
                field.set(instance, rs.getObject(field.getName()));
            }

            return instance;
        } catch (Exception e) {
            throw new SQLObjectConstructException();
        }
    }
}
