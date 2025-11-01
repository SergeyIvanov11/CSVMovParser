package org.example;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ObjectFactory {

    public <T> T getBean(Class<T> clazz) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        T obj = constructor.newInstance();

        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(MyBean.class)) {
                Object dependency;
                Class<?> type = f.getType();
                if (Map.class.isAssignableFrom(type)) {
                    dependency = new HashMap<>();
                } else if (List.class.isAssignableFrom(type)) {
                    dependency = new ArrayList<>();
                } else if (Set.class.isAssignableFrom(type)) {
                    dependency = new HashSet<>();
                } else {
                    dependency = getBean(type);
                }
                f.setAccessible(true);
                f.set(obj, dependency);
            }
            if (f.isAnnotationPresent(MyProperty.class)) {
                String value = f.getAnnotation(MyProperty.class).value();
                f.setAccessible(true);
                String propValue;
                if(value.equals("null")){
                    f.set(obj, null);
                } else {
                    propValue = System.getenv().get(value);
                    f.set(obj, propValue);
                }
            }
        }
        return obj;
    }
}
