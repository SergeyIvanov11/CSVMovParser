package org.example;

public interface MovieParser {

    Movie parseFromCSVLine(String[] line);
}
