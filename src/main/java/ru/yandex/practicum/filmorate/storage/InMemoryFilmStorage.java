package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmWithGenres;

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
    public FilmWithGenres findFilmById(Long filmId) {
        return filmDao.findFilmById(filmId);
    }

    @Override
    public void updateFilm(Film film) {
        filmDao.updateFilm(film);
    }

    @Override
    public List<FilmWithGenres> findAllFilms() {
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
    public List<Film> findMostPopularFilms(long listSize){
        return filmDao.getPopularFilms(listSize);
    }
}