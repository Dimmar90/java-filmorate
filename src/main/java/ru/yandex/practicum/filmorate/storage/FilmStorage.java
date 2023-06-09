package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Map;

public interface FilmStorage {
    void addFilm(@RequestBody @Valid Film film);

    void updateFilm(@RequestBody @Valid Film film);

    Map<Long, Film> getAllFilms();
}
