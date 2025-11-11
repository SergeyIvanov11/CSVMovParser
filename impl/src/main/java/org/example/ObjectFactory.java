package org.example;

import org.example.CustomSpring.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public class ObjectFactory {

    private final List<AnnotationHandler> handlers = new ArrayList<>();

    public ObjectFactory() {
        handlers.add(new MyBeanAnnotationHandler());
        handlers.add(new MyPropertyAnnotationHandler());
    }


    public <T> T getBean(Class<T> clazz) throws Exception {
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        T obj = constructor.newInstance();

        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            for (AnnotationHandler handler : handlers) {
                if (handler.matches(f)) {
                    handler.handle(this, obj, f);
                    break;
                }
            }
        }
        return obj;
    }
}
