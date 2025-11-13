package org.example.CustomSpring;

import org.example.ObjectFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public interface AnnotationHandler {
    boolean matches(Field field);

    void handle(ObjectFactory factory, Object obj, Field field) throws Exception;
}
