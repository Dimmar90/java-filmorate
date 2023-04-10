package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ErrorException;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashMap;
import java.util.Map;

@Service
@Component
@RestController
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private Map<Long, Long> likesMap = new HashMap<>();

    @ExceptionHandler(value = ErrorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleWrongUserUpdateException(ErrorException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @GetMapping(value = "/films/{id}")
    public ResponseEntity<?> getFilmById(@PathVariable long id) {
        boolean isFilmAdded = false;
        for (Film film : filmStorage.getFilms()) {
            if (film.getId() == id) {
                isFilmAdded = true;
                break;
            }
        }
        if (!isFilmAdded) {
            log.warn("Incorrect film id");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("Film id not found")), HttpStatus.NOT_FOUND);
        } else {
            log.debug("Get Film");
            return new ResponseEntity<>(filmStorage.getFilms().stream().filter(x -> x.getId() == id).findFirst(), HttpStatus.OK);
        }
    }

    @PutMapping(path = "/films/{id}/like/{userId}")
    public ResponseEntity<?> addLike(@PathVariable long id, @PathVariable long userId) {
        boolean isFilmAdded = false;
        boolean isUserAdded = false;

        for (Film film : filmStorage.getFilms()) {
            if (film.getId() == id) {
                isFilmAdded = true;
                break;
            }
        }
        for (User user : userStorage.getUsers()) {
            if (user.getId() == userId) {
                isUserAdded = true;
                break;
            }
        }

        if (!isFilmAdded) {
            log.warn("Wrong film id");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("Wrong film id")), HttpStatus.NOT_FOUND);
        }
        if (!isUserAdded) {
            log.warn("Wrong user id");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("Wrong user id")), HttpStatus.NOT_FOUND);
        }

        if (!likesMap.containsKey(id)) {
            likesMap.put(id, 1L);
            log.warn("Add like");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("Add like")), HttpStatus.OK);
        } else {
            Long likes = likesMap.get(id) + 1;
            likesMap.put(id, likes);
            log.warn("Add like");
            return new ResponseEntity<>("Add like", HttpStatus.OK);
        }
    }
}