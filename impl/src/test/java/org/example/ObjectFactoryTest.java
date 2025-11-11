package org.example;

import org.example.CustomSpring.MyBean;
import org.example.CustomSpring.MyBeanAnnotationHandler;
import org.example.CustomSpring.MyProperty;
import org.example.CustomSpring.MyPropertyAnnotationHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ObjectFactoryTest {
    static class TestClass1 {
    }

    static class TestClass2 {
        @MyBean
        private TestClass1 testClass;
        @MyBean
        private Map<String, Integer> map;
        @MyProperty("JAVA_HOME")
        private String javaHome;
        @MyProperty("null")
        private String nullable;
    }

    ObjectFactory factory;

    @BeforeEach
    void setUp() {
        factory = new ObjectFactory();
    }

    @Test
    void testMatchesBeanAnnotation() throws Exception {
        Field field = TestClass2.class.getDeclaredField("testClass");
        MyBeanAnnotationHandler handler = new MyBeanAnnotationHandler();

        assertTrue(handler.matches(field));
    }

    @Test
    void testHandleInjectsCorrectType() throws Exception {
        MyBeanAnnotationHandler handler = new MyBeanAnnotationHandler();
        TestClass2 obj = new TestClass2();
        Field field = TestClass2.class.getDeclaredField("testClass");

        handler.handle(factory, obj, field);

        field.setAccessible(true);
        Object value = field.get(obj);

        assertNotNull(value);
        assertEquals(TestClass1.class, value.getClass());
    }

    @Test
    void testHandleInjectsCollections() throws Exception {
        TestClass2 obj = new TestClass2();
        for (Field f : TestClass2.class.getDeclaredFields()) {
            new MyBeanAnnotationHandler().handle(factory, obj, f);
        }

        Field mapField = TestClass2.class.getDeclaredField("map");
        mapField.setAccessible(true);
        Object map = mapField.get(obj);
        assertTrue(map instanceof Map);
    }

    @Test
    void testMatchesPropertyAnnotation() throws Exception {
        Field field = TestClass2.class.getDeclaredField("javaHome");
        MyPropertyAnnotationHandler handler = new MyPropertyAnnotationHandler();

        assertTrue(handler.matches(field));
    }

    @Test
    void testHandleInjectsEnvironmentProperty() throws Exception {
        MyPropertyAnnotationHandler handler = new MyPropertyAnnotationHandler();
        TestClass2 obj = new TestClass2();
        Field field = TestClass2.class.getDeclaredField("javaHome");

        handler.handle(factory, obj, field);

        field.setAccessible(true);
        String value = (String) field.get(obj);

        // JAVA_HOME должна быть определена в окружении
        assertNotNull(value);
        assertTrue(value.contains("Java") || value.contains("java") || value.length() > 2);
    }

    @Test
    void testHandleSetsNullProperty() throws Exception {
        MyPropertyAnnotationHandler handler = new MyPropertyAnnotationHandler();
        TestClass2 obj = new TestClass2();
        Field field = TestClass2.class.getDeclaredField("nullable");

        handler.handle(factory, obj, field);

        field.setAccessible(true);
        assertNull(field.get(obj));
    }

    @Test
    void testFactoryInjectsBeansAutomatically() throws Exception {
        TestClass2 obj = factory.getBean(TestClass2.class);

        assertNotNull(obj);

        Field depField = TestClass2.class.getDeclaredField("testClass");
        depField.setAccessible(true);
        assertNotNull(depField.get(obj));
    }

    @Test
    void testFactoryInjectsPropertiesAutomatically() throws Exception {
        TestClass2 obj = factory.getBean(TestClass2.class);

        assertNotNull(obj);

        Field f1 = TestClass2.class.getDeclaredField("javaHome");
        f1.setAccessible(true);
        assertNotNull(f1.get(obj));

        Field f2 = TestClass2.class.getDeclaredField("nullable");
        f2.setAccessible(true);
        assertNull(f2.get(obj));
    }
}