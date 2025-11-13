package org.example.CustomSpring;

import org.example.ObjectFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class MyPropertyAnnotationHandler implements AnnotationHandler {

    private final EnvironmentVariableGetter envVarGetter;

    public MyPropertyAnnotationHandler(EnvironmentVariableGetter envVarGetter) {
        this.envVarGetter = envVarGetter;
    }
    @Override
    public boolean matches(Field field) {
        return field.isAnnotationPresent(MyProperty.class);
    }

    @Override
    public void handle(ObjectFactory factory, Object obj, Field field) throws Exception {
        String value = field.getAnnotation(MyProperty.class).value();
        field.setAccessible(true);
        String propValue;
        if(value.equals("null")){
            field.set(obj, null);
        } else {
            propValue = envVarGetter.get(value);
            field.set(obj, propValue);
        }
    }
}
