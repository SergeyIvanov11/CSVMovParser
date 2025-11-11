package org.example;

import org.example.CustomSpring.*;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.*;

public class MovieAnalyserImpl implements MovieAnalyser {
    @MyBean
    Map<String, Integer> genreCount;
    @MyBean
    Map<String, Integer> languageCount;
    @MyBean
    Map<Integer, Movie> highestRated;
    @MyBean
    Map<Integer, Movie> lowestRated;
    final Queue<Movie> top5 = new PriorityQueue<>(Comparator.comparingDouble(m -> m.imdb));
    @MyProperty(value = "null")
    Movie oldest;
    @MyProperty(value = "null")
    Movie newest;
    Duration totalMinutes = Duration.ofMinutes(0);

    @Override
    public void analyze(Movie m) {
        genreCount.merge(m.genre, 1, Integer::sum);
        languageCount.merge(m.language, 1, Integer::sum);
        updateOldestNewest(m);
        updateHighestLowestRated(m);
        updateTop5(m);
        totalMinutes = totalMinutes.plusMinutes(m.runtimeMinutes);
    }

    @Override
    public void printResults() throws UnsupportedEncodingException {
        // System.setOut(new PrintStream(System.out, true, "UTF-8"));
        System.out.println("Количество фильмов по жанрам:");
        genreCount.entrySet().stream()
                .sorted(Comparator.comparingInt((Map.Entry<String, Integer> e) -> e.getValue()).reversed())
                .forEach(e -> System.out.println(" - " + e.getKey() + ": " + e.getValue()));

        System.out.println("\nКоличество фильмов по языкам:");
        languageCount.entrySet().stream()
                .sorted(Comparator.comparingInt((Map.Entry<String, Integer> e) -> e.getValue()).reversed())
                .forEach(e -> System.out.println(" - " + e.getKey() + ": " + e.getValue()));

        System.out.println("\nТоп-5 фильмов по рейтингу IMDB:");
        top5.stream()
                .sorted(Comparator.comparingDouble((Movie m) -> m.imdb).reversed())
                .forEach(m -> System.out.printf(" %.1f — %s (%s)%n", m.imdb, m.title, m.premiere));

        if (oldest != null)
            System.out.printf("%nСамый старый фильм: %s (%s)%n", oldest.title, oldest.premiere);
        if (newest != null)
            System.out.printf("Самый новый фильм: %s (%s)%n", newest.title, newest.premiere);

        long totalHours = totalMinutes.toHours();
        long totalDays = totalMinutes.toDays();
        System.out.printf("%nОбщая длительность всех фильмов: %d часов (%d дней)%n", totalHours, totalDays);

        System.out.println("\nСамые высоко и низко оцененные фильмы по годам:");
        highestRated.forEach((year, movie) -> {
            Movie low = lowestRated.get(year);
            System.out.printf(" %d: ↑ %.1f — %s | ↓ %.1f — %s%n",
                    year, movie.imdb, movie.title, low.imdb, low.title);
        });
    }

    private void updateOldestNewest(Movie m) {
        if (m.premiere == null) return;
        if (oldest == null || m.premiere.isBefore(oldest.premiere)) {
            oldest = m;
            return;
        }
        if (newest == null || m.premiere.isAfter(newest.premiere)) newest = m;
    }

    private void updateHighestLowestRated(Movie m) {
        if (m.premiere == null) return;
        int year = m.premiere.getYear();

        highestRated.merge(year, m,
                (old, cur) -> cur.imdb > old.imdb ? cur : old);

        lowestRated.merge(year, m,
                (old, cur) -> cur.imdb < old.imdb ? cur : old);
    }

    private void updateTop5(Movie m) {
        top5.offer(m);
        if (top5.size() > 5) {
            top5.poll(); // убираем наименьший рейтинг
        }
    }

}
