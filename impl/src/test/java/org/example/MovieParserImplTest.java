package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MovieParserImplTest {

    private MovieParserImpl parser;

    @BeforeEach
    void setUp() {
        parser = new MovieParserImpl();
    }

    @Test
    void parseFromCSVLine_validLine_parsesCorrectly() {
        String[] line = {
                "\"Inception\"",
                "Sci-Fi",
                "\"July 16, 2010\"",
                "148 min",
                "8.8",
                "English"
        };

        Movie movie = parser.parseFromCSVLine(line);

        assertNotNull(movie);
        assertEquals("Inception", movie.title);
        assertEquals("Sci-Fi", movie.genre);
        assertEquals("English", movie.language);
        assertEquals(LocalDate.of(2010, 7, 16), movie.premiere);
        assertEquals(148, movie.runtimeMinutes);
        assertEquals(8.8, movie.imdb, 0.0001);
    }

    @Test
    void parseFromCSVLineWithEmptyDate() {
        String[] line = {
                "\"Avatar\"",
                "Adventure",
                "",
                "162 min",
                "7.9",
                "English"
        };

        Movie movie = parser.parseFromCSVLine(line);

        assertNotNull(movie);
        assertNull(movie.premiere);
        assertEquals(162, movie.runtimeMinutes);
        assertEquals(7.9, movie.imdb, 0.0001);
    }

    @Test
    void parseFromCSVLineWithIncorrectLine() {
        String[] line = { "\"Short line\"", "Drama" };

        Movie movie = parser.parseFromCSVLine(line);
        assertNull(movie);
    }

}