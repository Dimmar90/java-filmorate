package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Optional;

public interface FilmDao {

    void setFilmId(Film film);

    void addFilm(Film film);

    Optional<Film> findFilmById(long filmID);

    void updateFilm(Film film);

    void addLike(Long filmId);

    void deleteLike(long filmId);
}