//package ru.yandex.practicum.filmorate.service;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.*;
//import ru.yandex.practicum.filmorate.exception.ErrorException;
//import ru.yandex.practicum.filmorate.exception.ErrorResponse;
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.model.User;
//import ru.yandex.practicum.filmorate.storage.FilmStorage;
//import ru.yandex.practicum.filmorate.storage.UserStorage;
//
//import java.util.*;
//
//@Service
//@Component
//@RestController
//public class FilmService {
//    private final FilmStorage filmStorage;
//    private final UserStorage userStorage;
//    private static final Logger log = LoggerFactory.getLogger(UserService.class);
//    private final Comparator<Film> filmComparator = (film1, film2) -> (int) (film2.getRate() - film1.getRate());
//
//    @ExceptionHandler(value = ErrorException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ErrorResponse handleWrongUserUpdateException(ErrorException ex) {
//        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
//    }
//
//    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
//        this.filmStorage = filmStorage;
//        this.userStorage = userStorage;
//    }
//
//    @GetMapping(value = "/films/{id}")
//    public ResponseEntity<?> getFilmById(@PathVariable long id) {
//        boolean isFilmAdded = false;
//        for (Film film : filmStorage.getFilms()) {
//            if (film.getId() == id) {
//                isFilmAdded = true;
//                break;
//            }
//        }
//        if (!isFilmAdded) {
//            log.warn("Incorrect film id");
//            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("Film id not found")), HttpStatus.NOT_FOUND);
//        } else {
//            log.debug("Get Film");
//            return new ResponseEntity<>(filmStorage.getFilms().stream().filter(x -> x.getId() == id).findFirst(), HttpStatus.OK);
//        }
//    }
//
//    public void addLikeInRate(long id) {
//        for (Film film : filmStorage.getFilms()) {
//            if (film.getId() == id) {
//                long amountOfLikes = film.getRate() + 1;
//                film.setRate(amountOfLikes);
//            }
//        }
//    }
//
//    @PutMapping(path = "/films/{id}/like/{userId}")
//    public ResponseEntity<?> addLike(@PathVariable long id, @PathVariable long userId) {
//        boolean isFilmAdded = false;
//        boolean isUserAdded = false;
//
//        for (Film film : filmStorage.getFilms()) {
//            if (film.getId() == id) {
//                isFilmAdded = true;
//                break;
//            }
//        }
//        for (User user : userStorage.getUsers()) {
//            if (user.getId() == userId) {
//                isUserAdded = true;
//                break;
//            }
//        }
//
//        if (!isFilmAdded) {
//            log.warn("Wrong film id");
//            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("Wrong film id")), HttpStatus.NOT_FOUND);
//        }
//        if (!isUserAdded) {
//            log.warn("Wrong user id");
//            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("Wrong user id")), HttpStatus.NOT_FOUND);
//        }
//        addLikeInRate(id);
//        log.debug("Add like");
//        return new ResponseEntity<>("Add like", HttpStatus.OK);
//    }
//
//    public void deleteLikeInRate(long id) {
//        for (Film film : filmStorage.getFilms()) {
//            if (film.getId() == id) {
//                long amountOfLikes = film.getRate() - 1;
//                film.setRate(amountOfLikes);
//            }
//        }
//    }
//
//    @DeleteMapping(path = "/films/{id}/like/{userId}")
//    public ResponseEntity<?> deleteLike(@PathVariable long id, @PathVariable long userId) {
//        boolean isFilmAdded = false;
//        boolean isUserAdded = false;
//
//        for (Film film : filmStorage.getFilms()) {
//            if (film.getId() == id) {
//                isFilmAdded = true;
//                break;
//            }
//        }
//        for (User user : userStorage.getUsers()) {
//            if (user.getId() == userId) {
//                isUserAdded = true;
//                break;
//            }
//        }
//
//        if (!isFilmAdded) {
//            log.warn("Wrong film id");
//            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("Wrong film id")), HttpStatus.NOT_FOUND);
//        }
//        if (!isUserAdded) {
//            log.warn("Wrong user id");
//            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("Wrong user id")), HttpStatus.NOT_FOUND);
//        }
//        deleteLikeInRate(id);
//        log.debug("Delete like");
//        return new ResponseEntity<>("Add like", HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/films/popular")
//    public ResponseEntity<?> getPopularFilms(@RequestParam(required = false) String count) {
//        int maxSize;
//        if (count == null) {
//            maxSize = 10;
//        } else {
//            maxSize = Integer.parseInt(count);
//        }
//        List<Film> listOfFilms = new ArrayList<>(filmStorage.getFilms());
//        log.debug("Get most popular films");
//        return new ResponseEntity<>(listOfFilms.stream().sorted(filmComparator).limit(maxSize), HttpStatus.OK);
//    }
//}