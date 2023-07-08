package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

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
}
