
package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ErrorException;
import ru.yandex.practicum.filmorate.exception.ErrorHandler;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final LocalDate date = LocalDate.of(1895, Month.DECEMBER, 28);
    private final Comparator<Film> filmComparator = (film1, film2) -> (int) (film2.getRate() - film1.getRate());

    private final ErrorHandler errorHandler = new ErrorHandler();

    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public ResponseEntity<?> addFilm(Film film) {
        if (film.getReleaseDate().isBefore(date)) {
            log.warn("Wrong Release Date Of Film");
            return new ResponseEntity<>(errorHandler.handleException(new ErrorException("Wrong Release Date Of Film")), HttpStatus.BAD_REQUEST);
        }
        inMemoryFilmStorage.addFilm(film);
        log.debug("Film Added: {}", film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    public ResponseEntity<?> updateFilm(Film film) {
        if (inMemoryFilmStorage.getAllFilms().containsKey(film.getId())) {
            inMemoryFilmStorage.updateFilm(film);
            log.debug("Film Updated: {}", film.getName());
            return new ResponseEntity<>(film, HttpStatus.OK);
        } else {
            log.error("Wrong Film Id");
            return new ResponseEntity<>(errorHandler.handleException(new ErrorException("User ID Not Found")), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getAllFilms() {
        log.debug("Get All Films: {}", inMemoryFilmStorage.getAllFilms().values());
        return new ResponseEntity<>(inMemoryFilmStorage.getAllFilms().values(), HttpStatus.OK);
    }

    public ResponseEntity<?> getFilmById(long filmID) {
        if (inMemoryFilmStorage.getAllFilms().containsKey(filmID)) {
            return new ResponseEntity<>(inMemoryFilmStorage.getAllFilms().get(filmID), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(errorHandler.handleException(new ErrorException("Film Id Not Found")), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> addLike(long filmID, long userId) {

        boolean isFilmAdded = false;
        boolean isUserAdded = false;

        if (inMemoryFilmStorage.getAllFilms().containsKey(filmID)) {
            isFilmAdded = true;
        }
        if (inMemoryUserStorage.getUsers().containsKey(userId)) {
            isUserAdded = true;
        }

        if (!isFilmAdded) {
            log.warn("Wrong Film Id");
            return new ResponseEntity<>(errorHandler.handleException(new ErrorException("Wrong Film Id")), HttpStatus.NOT_FOUND);
        }
        if (!isUserAdded) {
            log.warn("Wrong User Id");
            return new ResponseEntity<>(errorHandler.handleException(new ErrorException("Wrong User Id")), HttpStatus.NOT_FOUND);
        }
        inMemoryFilmStorage.getFilmById(filmID).setRate(inMemoryFilmStorage.getFilmById(filmID).getRate() + 1);
        log.debug("Like Added");
        return new ResponseEntity<>("Like Added", HttpStatus.OK);
    }

    public ResponseEntity<?> deleteLike(long filmID, long userID) {

        boolean isFilmAdded = false;
        boolean isUserAdded = false;

        if (inMemoryFilmStorage.getAllFilms().containsKey(filmID)) {
            isFilmAdded = true;
        }
        if (inMemoryUserStorage.getUsers().containsKey(userID)) {
            isUserAdded = true;
        }

        if (!isFilmAdded) {
            log.warn("Wrong Film Id");
            return new ResponseEntity<>(errorHandler.handleException(new ErrorException("Wrong Film Id")), HttpStatus.NOT_FOUND);
        }
        if (!isUserAdded) {
            log.warn("Wrong User Id");
            return new ResponseEntity<>(errorHandler.handleException(new ErrorException("Wrong User Id")), HttpStatus.NOT_FOUND);
        }
        inMemoryFilmStorage.getFilmById(filmID).setRate(inMemoryFilmStorage.getFilmById(filmID).getRate() - 1);
        log.debug("Like Deleted");
        return new ResponseEntity<>("Like Deleted", HttpStatus.OK);
    }

    public ResponseEntity<?> getPopularFilms(String count) {
        int maxSize;
        if (count == null) {
            maxSize = 10;
        } else {
            maxSize = Integer.parseInt(count);
        }
        List<Film> listOfFilms = new ArrayList<>(inMemoryFilmStorage.getAllFilms().values());
        log.debug("Get most popular films :{}", listOfFilms.stream().sorted(filmComparator).limit(maxSize));
        return new ResponseEntity<>(listOfFilms.stream().sorted(filmComparator).limit(maxSize), HttpStatus.OK);
    }
}