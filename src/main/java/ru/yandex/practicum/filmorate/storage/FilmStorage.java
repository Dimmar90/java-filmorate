package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmWithGenres;

import java.util.List;

public interface FilmStorage {
    void addFilm(Film film);

    FilmWithGenres findFilmById(Long filmId);

    void updateFilm(Film film);

    List<FilmWithGenres> findAllFilms();

    void addLike(Long filmId);

    void deleteLike(Long filmId);

    List<Film> findMostPopularFilms(long listSize);
}