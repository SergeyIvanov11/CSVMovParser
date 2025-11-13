package org.example.CustomSpring;

public class EnvironmentVariableGetter {

    public String get(String key) {
        return System.getenv(key);
    }
}
