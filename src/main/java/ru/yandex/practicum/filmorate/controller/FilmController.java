package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @PostMapping(value = "/film")
    public Film create(@RequestBody Film film) {
        return filmValidation(film);
    }

    @PatchMapping(value = "/film")
    public Film update(@RequestBody Film film) {
        return filmValidation(film);
    }

    @GetMapping("/films")
    public Map<Integer, Film> findAll() {
        log.debug("Get all films: {}", films);
        return films;
    }

    public Film filmValidation(Film film) {
        try {
            if (film.getName() == null || film.getName().isBlank()) {
                throw new ValidationException("Missing film name");
            }
        } catch (ValidationException e) {
            log.error(e.getMessage());
            return null;
        }
        try {
            if (film.getDescription().length() > 200) {
                throw new ValidationException("Description film limit");
            }
        } catch (ValidationException e) {
            log.error(e.getMessage());
            return null;
        }
        try {
            LocalDate date = LocalDate.of(1895, Month.DECEMBER, 28);
            if (film.getReleaseDate().isBefore(date)) {
                throw new ValidationException("Wrong release date of film");
            }
        } catch (ValidationException e) {
            log.error(e.getMessage());
            return null;
        }
        try {
            if (film.getDuration() < 1) {
                throw new ValidationException("Wrong duration of film");
            }
        } catch (ValidationException e) {
            log.error(e.getMessage());
            return null;
        }
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("Film updated: {}", film.getName());
        } else {
            film.setId(id++);
            films.put(film.getId(), film);
            log.debug("Film Added: {}", film.getName());
        }
        return film;
    }
}
