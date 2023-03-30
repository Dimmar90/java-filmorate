package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ErrorException;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;

import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FilmController {

    private final List<Film> films = new ArrayList<>();
    private int id = 0;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @ExceptionHandler(value = ErrorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse filmErrorException(ErrorException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @PostMapping(value = "/films")
    public ResponseEntity<?> create(@RequestBody @Valid Film film) {
        LocalDate date = LocalDate.of(1895, Month.DECEMBER, 28);

        if (film.getName().isEmpty() || film.getName().isBlank()) {
            log.warn("Empty film name");
            return new ResponseEntity<>(filmErrorException(new ErrorException("Empty film name")), HttpStatus.NOT_FOUND);
        }
        if (film.getDescription().length() > 200) {
            log.warn("Description film limit");
            return new ResponseEntity<>(filmErrorException(new ErrorException("Description film limit")), HttpStatus.NOT_FOUND);
        }
        if (film.getReleaseDate().isBefore(date)) {
            log.warn("Wrong release date of film");
            return new ResponseEntity<>(filmErrorException(new ErrorException("Wrong release date of film")), HttpStatus.NOT_FOUND);
        }
        if (film.getDuration() < 1) {
            log.warn("Wrong duration of film");
            return new ResponseEntity<>(filmErrorException(new ErrorException("Wrong duration of film")), HttpStatus.NOT_FOUND);
        }
        film.setId(++id);
        films.add(film);
        log.debug("Film added: {}", film.getName());
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        log.debug("Get all films: {}", films);
        return films;
    }
}
