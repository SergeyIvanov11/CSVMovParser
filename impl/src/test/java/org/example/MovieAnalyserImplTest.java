package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class MovieAnalyserImplTest {
    private MovieAnalyserImpl analyser;
    private Movie m1;
    private Movie m2;
    private Movie m3;

    @BeforeEach
    void setUp() {
        analyser = new MovieAnalyserImpl();
        analyser.genreCount = new HashMap<>();
        analyser.languageCount = new HashMap<>();
        analyser.highestRated = new HashMap<>();
        analyser.lowestRated = new HashMap<>();
        m1 = new Movie("Inception", "Sci-Fi", "English", LocalDate.of(2010, 7, 16), 148, 8.8);
        m2 = new Movie("Avatar", "Adventure", "English", LocalDate.of(2009, 12, 18), 162, 7.9);
        m3 = new Movie("Amelie", "Romance", "French", LocalDate.of(2001, 4, 25), 122, 8.3);
    }

    @Test
    void analyzeGenreAndLanguageCounts() {
        analyser.analyze(m1);
        analyser.analyze(m2);
        analyser.analyze(m3);

        assertEquals(1, analyser.genreCount.get("Sci-Fi"));
        assertEquals(1, analyser.genreCount.get("Romance"));
        assertEquals(1, analyser.genreCount.get("Adventure"));
        assertEquals(2, analyser.languageCount.get("English"));
        assertEquals(1, analyser.languageCount.get("French"));
    }

    @Test
    void analyzeOldestAndNewest() {
        analyser.analyze(m2);
        analyser.analyze(m1);
        analyser.analyze(m3);

        assertEquals("Amelie", analyser.oldest.title);
        assertEquals("Inception", analyser.newest.title);
    }

    @Test
    void analyzeTotalDuration() {
        analyser.analyze(m1);
        analyser.analyze(m2);

        Duration total = analyser.totalMinutes;
        assertEquals(Duration.ofMinutes(310), total);
    }
}