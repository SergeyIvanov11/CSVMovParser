package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        ObjectFactory factory = new ObjectFactory();
        MovieParserImpl movieParser = new MovieParserImpl();
        MovieAnalyserImpl movieAnalyser = factory.getBean(MovieAnalyserImpl.class);
        String fileName = "NetflixOriginals.csv";

        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            List<String[]> rows = reader.readAll();
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                Movie m = movieParser.parseFromCSVLine(row);
                movieAnalyser.analyze(m);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        movieAnalyser.printResults();
    }
}
