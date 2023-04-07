package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ErrorException;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@RestController
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<Long, Film> films = new HashMap<>();
    private long id = 0;
    private static final Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);
    private LocalDate date = LocalDate.of(1895, Month.DECEMBER, 28);

    @ExceptionHandler(value = ErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFilmErrorException(ErrorException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @PostMapping(value = "/films")
    public ResponseEntity<?> createFilm(@RequestBody @Valid Film film) {
        if (film.getReleaseDate().isBefore(date)) {
            log.warn("Wrong release date of film");
            return new ResponseEntity<>(handleFilmErrorException(new ErrorException("Wrong release date Of Film")), HttpStatus.BAD_REQUEST);
        }
        film.setId(++id);
        films.put(film.getId(), film);
        log.debug("Film added: {}", film.getName());
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @PutMapping(value = "/films")
    public ResponseEntity<?> updateFilm(@RequestBody @Valid Film film) {
        LocalDate date = LocalDate.of(1895, Month.DECEMBER, 28);
        long index = -1;

        if (films.keySet().contains(film.getId())) {
            index = film.getId();
        }

        if (index == -1) {
            log.debug("Film ID not found");
            return new ResponseEntity<>(handleFilmErrorException(new ErrorException("Film ID not found")), HttpStatus.NOT_FOUND);
        } else if (film.getReleaseDate().isBefore(date)) {
            log.debug("Wrong release date of film");
            return new ResponseEntity<>(handleFilmErrorException(new ErrorException("Wrong release date of film")), HttpStatus.BAD_REQUEST);
        }
        films.remove(film);
        films.put(film.getId(), film);
        log.debug("Film update: {}", film.getName());
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @GetMapping(value = "/films")
    public Collection<Film> getFilms() {
        log.debug("Get all films: {}", films);
        return films.values();
    }
}
