
package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ErrorException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Service
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;
    private final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final LocalDate firstFilmRelease = LocalDate.of(1895, Month.DECEMBER, 28);

    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addFilm(Film film) {
        if (film.getReleaseDate().isBefore(firstFilmRelease)) {
            String msg = "Wrong Release Date Of Film";
            log.warn(msg);
            throw new ErrorException(msg);
        }
        inMemoryFilmStorage.addFilm(film);
        log.debug("Film Added: {}", film);
    }

    public void updateFilm(Film film) {
        if (inMemoryFilmStorage.findFilmById(film.getId()).isEmpty()) {
            String msg = "Film With This Id Does Not Exist";
            log.warn(msg);
            throw new ErrorException(msg);
        } else {
            inMemoryFilmStorage.updateFilm(film);
            log.debug("Film Updated");
        }
    }

    public List<Film> getAllFilms() {
        log.debug("Get All Films");
        return inMemoryFilmStorage.findAllFilms();
    }

    public Optional<Film> getFilmById(long filmID) {
        if (inMemoryFilmStorage.findFilmById(filmID).isEmpty()) {
            String msg = "Film With This Id Does Not Exist";
            log.warn(msg);
            throw new ErrorException(msg);
        }
        log.debug("Get Film By Id");
        return inMemoryFilmStorage.findFilmById(filmID);
    }

    public void addLike(long filmID, long userId) {
        if (inMemoryFilmStorage.findFilmById(filmID).isEmpty()) {
            String msg = "Wrong Film Id";
            log.warn(msg);
            throw new ErrorException(msg);
        }
        if (inMemoryUserStorage.findUserById(userId).isEmpty()) {
            String msg = "Wrong User Id";
            log.warn(msg);
            throw new ErrorException(msg);
        }
        inMemoryFilmStorage.addLike(filmID);
        log.debug("Like Added");
    }

    public void deleteLike(long filmID, long userId) {
        if (inMemoryFilmStorage.findFilmById(filmID).isEmpty()) {
            String msg = "Wrong Film Id";
            log.warn(msg);
            throw new ErrorException(msg);
        }
        if (inMemoryUserStorage.findUserById(userId).isEmpty()) {
            String msg = "Wrong User Id";
            log.warn(msg);
            throw new ErrorException(msg);
        }
        inMemoryFilmStorage.deleteLike(filmID);
        log.debug("Like Deleted");
    }

    public List<Film> getPopularFilms(String count) {
        log.debug("Get Popular Films");
        return inMemoryFilmStorage.findMostPopularFilms(count);
    }
}