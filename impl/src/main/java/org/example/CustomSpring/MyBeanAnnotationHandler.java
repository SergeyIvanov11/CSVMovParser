package org.example.CustomSpring;

import org.example.ObjectFactory;

import java.lang.reflect.Field;
import java.util.*;

public class MyBeanAnnotationHandler implements AnnotationHandler {
    @Override
    public boolean matches(Field field) {
        return field.isAnnotationPresent(MyBean.class);
    }

    @Override
    public void handle(ObjectFactory factory, Object obj, Field field) throws Exception {
        Object dependency;
        Class<?> type = field.getType();
        if (Map.class.isAssignableFrom(type)) {
            dependency = new HashMap<>();
        } else if (List.class.isAssignableFrom(type)) {
            dependency = new ArrayList<>();
        } else if (Set.class.isAssignableFrom(type)) {
            dependency = new HashSet<>();
        } else {
            dependency = factory.getBean(type);
        }
        field.setAccessible(true);
        field.set(obj, dependency);
    }
}
