package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    void addFilm(Film film);

    Optional<Film> findFilmById(Long filmId);

    void updateFilm(Film film);

    List<Film> findAllFilms();

    void addLike(Long filmId);

    void deleteLike(Long filmId);

    List<Film> findMostPopularFilms(String count);
}