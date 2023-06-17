package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Map;

@RestController

public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping(value = "/films")
    public ResponseEntity<?> addFilm(@RequestBody @Valid Film film) {
        filmService.addFilm(film);
        return new ResponseEntity<>(film, HttpStatus.CREATED);
    }

    @PutMapping(value = "/films")
    public ResponseEntity<?> updateFilm(@RequestBody @Valid Film film) {
        filmService.updateFilm(film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @GetMapping(value = "/films")
    public ResponseEntity<?> getAllFilms(@RequestBody @Valid Film film) {
        return new ResponseEntity<>(filmService.getAllFilms(), HttpStatus.OK);
    }

    @GetMapping(value = "/films/{id}")
    public ResponseEntity<?> getFilmById(@PathVariable long id) {
        return new ResponseEntity<>(filmService.getFilmById(id), HttpStatus.OK);
    }

    @PutMapping(path = "/films/{id}/like/{userId}")
    public ResponseEntity<?> addLike(@PathVariable long id, @PathVariable long userId) {
        filmService.addLike(id, userId);
        return new ResponseEntity<>("Like Added", HttpStatus.OK);
    }

    @DeleteMapping(path = "/films/{id}/like/{userId}")
    public ResponseEntity<?> deleteLike(@PathVariable long id, @PathVariable long userId) {
        filmService.deleteLike(id, userId);
        return new ResponseEntity<>("Like Deleted", HttpStatus.OK);
    }

    @GetMapping(value = "/films/popular")
    public ResponseEntity<?> getPopularFilms(@RequestParam(required = false) String count) {
        return new ResponseEntity<>(filmService.getPopularFilms(count), HttpStatus.OK);
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> notFoundException(final IllegalStateException e) {
        return Map.of("Error", e.getMessage());
    }
}