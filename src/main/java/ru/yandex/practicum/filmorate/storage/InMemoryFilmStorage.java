package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static FilmDao filmDao;

    public InMemoryFilmStorage(FilmDao filmDao) {
        this.filmDao = filmDao;
    }

    @Override
    public void addFilm(Film film) {
        if (filmDao.findLastFilm() == null) {
            film.setId(1);
            filmDao.addFilm(film);
        } else {
            film.setId(filmDao.findLastFilm().getId() + 1);
            filmDao.addFilm(film);
        }
    }

    @Override
    public Film findFilmById(Long filmId) {
        return filmDao.findFilmById(filmId);
    }

    @Override
    public void updateFilm(Film film) {
        filmDao.updateFilm(film);
    }

    @Override
    public List<Film> findAllFilms() {
        return filmDao.getAllFilms();
    }

    @Override
    public void addLike(Long filmId) {
        filmDao.addLike(filmId);
    }

    @Override
    public void deleteLike(Long filmId) {
        filmDao.deleteLike(filmId);
    }

    @Override
    public List<Film> findMostPopularFilms(long listSize) {
        return filmDao.getPopularFilms(listSize);
    }

    @Override
    public List<Mpa> findAllMpa() {
        return filmDao.getAllMpa();
    }

    @Override
    public Mpa findMpaById(int mpaId) {
        return filmDao.getMpaById(mpaId);
    }

    @Override
    public List<Genre> findAllGenres() {
        return filmDao.getAllGenres();
    }

    @Override
    public Genre findGenreById(int genreId) {
        return filmDao.getGenreById(genreId);
    }
}