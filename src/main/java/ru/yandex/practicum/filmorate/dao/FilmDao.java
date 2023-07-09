package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface FilmDao {

    Film findLastFilm();

    void addFilm(Film film);

    Film findFilmById(long filmID);

    void updateFilm(Film film);

    List<Film> getAllFilms();

    void addLike(Long filmId);

    void deleteLike(long filmId);

    List<Film> getPopularFilms(long listSize);

    List<Mpa> getAllMpa();

    Mpa getMpaById(Integer mpaId);

    List<Genre> getAllGenres();

    Genre getGenreById(int genreId);
}
