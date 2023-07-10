package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.GenreService;

@RestController
public class GenreController {
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    private GenreService genreService;

    @GetMapping(value = "/genres")
    public ResponseEntity<?> getAllGenres() {
        return new ResponseEntity<>(genreService.getAllGenres(), HttpStatus.OK);
    }

    @GetMapping(value = "/genres/{id}")
    public ResponseEntity<?> getGenreById(@PathVariable int id) {
        return new ResponseEntity<>(genreService.getGenreById(id), HttpStatus.OK);
    }
}
