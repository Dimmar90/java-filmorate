package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface FilmStorage {
    void addFilm(Film film);

    Film findFilmById(Long filmId);

    void updateFilm(Film film);

    List<Film> findAllFilms();

    void addLike(Long filmId);

    void deleteLike(Long filmId);

    List<Film> findMostPopularFilms(long listSize);

    List<Mpa> findAllMpa();

    Mpa findMpaById(int mpaId);

    List<Genre> findAllGenres();

    Genre findGenreById(int genreId);
}