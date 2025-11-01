package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MovieParserImpl implements MovieParser {
    public final String REGEX = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
    public final String DATE_PATTERN = "MMMM d, yyyy";

    @Override
    public Movie parseFromCSVLine(String[] line) {
        try {
            String title = line[0].replace("\"", "").trim();
            String genre = line[1].trim();
            String premiereStr = line[2].replace("\"", "").replace(".", ",").trim();
            String runtimeStr = line[3].trim();
            String imdbStr = line[4].trim();
            String language = line[5].trim();

            LocalDate premiere = null;
            if (!premiereStr.isEmpty()) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.ENGLISH);
                    premiere = LocalDate.parse(premiereStr, formatter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            int runtime = 0;
            if (runtimeStr.endsWith("min")) {
                runtimeStr = runtimeStr.replace("min", "").trim();
            }
            if (!runtimeStr.isEmpty()) {
                runtime = Integer.parseInt(runtimeStr);
            }

            double imdb = 0.0;
            if (!imdbStr.isEmpty()) {
                imdb = Double.parseDouble(imdbStr);
            }

            return new Movie(title, genre, language, premiere, runtime, imdb);
        } catch (Exception e) {
            return null; // строка некорректная — пропускаем
        }
    }

}