package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long id = 0;

    @Override
    public void addFilm(Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
    }

    @Override
    public void updateFilm(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public Optional<Film> findFilmById(Long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void addLike(Long filmId) {
        films.get(filmId).setRate(films.get(filmId).getRate() + 1);
    }

    @Override
    public void deleteLike(Long filmId) {
        films.get(filmId).setRate(films.get(filmId).getRate() - 1);
    }

    @Override
    public List<Film> findMostPopularFilms(String count) {
        int maxSize;
        if (count == null) {
            maxSize = 10;
        } else {
            maxSize = Integer.parseInt(count);
        }
        return findAllFilms().stream()
                .sorted(Comparator.comparing(Film::getRate).reversed())
                .limit(maxSize).collect(Collectors.toList());
    }
}