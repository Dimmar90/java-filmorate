package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    void addFilm(Film film);

    Film findFilmById(Long filmId);

    void updateFilm(Film film);

    List<Film> findAllFilms();

    void addLike(Long filmId);

    void deleteLike(Long filmId);

    List<Film> findMostPopularFilms(long listSize);
}