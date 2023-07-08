package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ErrorException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Service
public class FilmService {
    private UserStorage userStorage;
    private FilmStorage filmStorage;
    private final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final LocalDate firstFilmRelease = LocalDate.of(1895,12,12);


    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addFilm(Film film) {
        if (film.getDuration() <= 0) {
            String msg = "Wrong Duration Of Film";
            log.warn(msg);
            throw new IllegalStateException(msg);
        }
        if (film.getReleaseDate().isBefore(firstFilmRelease)) {
            String msg = "Wrong Release Date Of Film";
            log.warn(msg);
            throw new IllegalStateException(msg);
        }
        filmStorage.addFilm(film);
        log.debug("Film Added: {}", film);
    }

    public Film getFilmById(long filmID) {
        if (filmStorage.findFilmById(filmID) == null) {
            String msg = "Film With This Id Does Not Exist";
            log.warn(msg);
            throw new ErrorException(msg);
        }
        log.debug("Get Film By Id");
        return filmStorage.findFilmById(filmID);
    }

    public void updateFilm(Film film) {
        if (filmStorage.findFilmById(film.getId()) == null) {
            String msg = "Film With This Id Does Not Exist";
            log.warn(msg);
            throw new ErrorException(msg);
        } else {
            filmStorage.updateFilm(film);
            log.debug("Film Updated");
        }
    }

    public List<Film> getAllFilms() {
        log.debug("Get All Films");
        return filmStorage.findAllFilms();
    }

    public void addLike(long filmID, long userId) {
        if (filmStorage.findFilmById(filmID) == null) {
            String msg = "Wrong Film Id";
            log.warn(msg);
            throw new ErrorException(msg);
        }
        if (filmStorage.findFilmById(filmID) == null) {
            String msg = "Wrong User Id";
            log.warn(msg);
            throw new ErrorException(msg);
        }
        filmStorage.addLike(filmID);
        log.debug("Like Added");
    }

    public void deleteLike(long filmID, long userId) {
        if (filmStorage.findFilmById(filmID) == null) {
            String msg = "Wrong Film Id";
            log.warn(msg);
            throw new ErrorException(msg);
        }
        if (userStorage.findUserById(userId) == null) {
            String msg = "Wrong User Id";
            log.warn(msg);
            throw new ErrorException(msg);
        }
        filmStorage.deleteLike(filmID);
        log.debug("Like Deleted");
    }

    public List<Film> getPopularFilms(String count) {
        if (count == null) {
            return filmStorage.findMostPopularFilms(10);
        } else {
            return filmStorage.findMostPopularFilms(Long.parseLong(count));
        }
    }
}