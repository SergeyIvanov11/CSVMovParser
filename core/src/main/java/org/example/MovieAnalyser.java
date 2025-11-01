package org.example;

import java.io.UnsupportedEncodingException;

public interface MovieAnalyser {

    void analyze(Movie m);

    void printResults() throws UnsupportedEncodingException;
}
