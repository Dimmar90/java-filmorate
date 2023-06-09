
package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exception.ErrorException;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;
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

    @ExceptionHandler(value = ErrorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmErrorException(ErrorException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public ResponseEntity<?> createFilm(Film film) {
        if (film.getReleaseDate().isBefore(date)) {
            log.warn("Wrong release date of film");
            return new ResponseEntity<>(handleFilmErrorException(new ErrorException("Wrong release date Of film")), HttpStatus.BAD_REQUEST);
        }
        inMemoryFilmStorage.addFilm(film);
        log.debug("Film added: {}", film.getName());
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    public ResponseEntity<?> updateFilm(Film film) {
        if (inMemoryFilmStorage.getAllFilms().containsKey(film.getId())){
            inMemoryFilmStorage.updateFilm(film);
            log.debug("Film updated: {}", film.getName());
            return new ResponseEntity<>(film, HttpStatus.OK);
        } else {
            log.error("Wrong Film Id");
            return new ResponseEntity<>(handleFilmErrorException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        }
    }

    public java.util.Collection<Film> getAllFilms() {
        log.debug("Get all films: {}", inMemoryFilmStorage.getAllFilms().values());
        return inMemoryFilmStorage.getAllFilms().values();
    }

    public ResponseEntity<?> getFilmById(long id) {
        boolean isFilmAdded = inMemoryFilmStorage.getAllFilms().containsKey(id);

        if (!isFilmAdded) {
            log.warn("Incorrect film id");
            return new ResponseEntity<>(handleFilmErrorException(new ErrorException("Film id not found")), HttpStatus.NOT_FOUND);
        } else {
            log.debug("Get Film");
            return new ResponseEntity<>(inMemoryFilmStorage.getAllFilms().values().stream().filter(x -> x.getId() == id).findFirst(), HttpStatus.OK);
        }
    }

    public ResponseEntity<?> addLike(long id, long userId) {

        boolean isFilmAdded = false;
        boolean isUserAdded = false;

        if (inMemoryFilmStorage.getAllFilms().containsKey(id)) {
            isFilmAdded = true;
        }
        if (inMemoryUserStorage.getAllUsers().containsKey(userId)) {
            isUserAdded = true;
        }

        if (!isFilmAdded) {
            log.warn("Wrong film id");
            return new ResponseEntity<>(handleFilmErrorException(new ErrorException("Wrong film id")), HttpStatus.NOT_FOUND);
        }
        if (!isUserAdded) {
            log.warn("Wrong user id");
            return new ResponseEntity<>(handleFilmErrorException(new ErrorException("Wrong user id")), HttpStatus.NOT_FOUND);
        }
        inMemoryFilmStorage.getAllFilms().get(id).setRate(inMemoryFilmStorage.getAllFilms().get(id).getRate() + 1);
        log.debug("Like Added");
        return new ResponseEntity<>("Add like", HttpStatus.OK);
    }


    public ResponseEntity<?> deleteLike(long id, long userId) {

        boolean isFilmAdded = false;
        boolean isUserAdded = false;

        if (inMemoryFilmStorage.getAllFilms().containsKey(id)) {
            isFilmAdded = true;
        }
        if (inMemoryUserStorage.getAllUsers().containsKey(userId)) {
            isUserAdded = true;
        }

        if (!isFilmAdded) {
            log.warn("Wrong film id");
            return new ResponseEntity<>(handleFilmErrorException(new ErrorException("Wrong film id")), HttpStatus.NOT_FOUND);
        }
        if (!isUserAdded) {
            log.warn("Wrong user id");
            return new ResponseEntity<>(handleFilmErrorException(new ErrorException("Wrong user id")), HttpStatus.NOT_FOUND);
        }
        inMemoryFilmStorage.getAllFilms().get(id).setRate(inMemoryFilmStorage.getAllFilms().get(id).getRate() - 1);
        log.debug("Like Deleted");
        return new ResponseEntity<>("Delete like", HttpStatus.OK);
    }


    public ResponseEntity<?> getPopularFilms(String count) {
        int maxSize;
        if (count == null) {
            maxSize = 10;
        } else {
            maxSize = Integer.parseInt(count);
        }
        List<Film> listOfFilms = new ArrayList<>(inMemoryFilmStorage.getAllFilms().values());
        log.debug("Get most popular films");
        return new ResponseEntity<>(listOfFilms.stream().sorted(filmComparator).limit(maxSize), HttpStatus.OK);
    }
}