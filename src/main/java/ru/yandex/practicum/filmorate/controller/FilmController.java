package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ErrorException;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;

@RestController
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping(value = "/films")
    public ResponseEntity<?> addFilm(@RequestBody @Valid Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping(value = "/films")
    public ResponseEntity<?> updateFilm(@RequestBody @Valid Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping(value = "/films")
    public ResponseEntity<?> getAllFilms(@RequestBody @Valid Film film) {
        return filmService.getAllFilms();
    }

    @GetMapping(value = "/films/{id}")
    public ResponseEntity<?> getFilmById(@PathVariable long id) {
        return filmService.getFilmById(id);
    }

    @PutMapping(path = "/films/{id}/like/{userId}")
    public ResponseEntity<?> addLike(@PathVariable long id, @PathVariable long userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping(path = "/films/{id}/like/{userId}")
    public ResponseEntity<?> deleteLike(@PathVariable long id, @PathVariable long userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping(value = "/films/popular")
    public ResponseEntity<?> getPopularFilms(@RequestParam(required = false) String count) {
        return filmService.getPopularFilms(count);
    }
}