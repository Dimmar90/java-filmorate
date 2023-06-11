package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ErrorException;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.Collection;

@RestController
public class UserController {
    private final UserService userService;
    private final InMemoryUserStorage inMemoryUserStorage;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, InMemoryUserStorage inMemoryUserStorage) {
        this.userService = userService;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @ExceptionHandler(value = ErrorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleWrongUserUpdateException(ErrorException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @PostMapping(value = "/users")
    public User createUser(@RequestBody @Valid User user) {
        return inMemoryUserStorage.addUser(user);
    }

    @PutMapping(value = "/users")
    public ResponseEntity<?> updateUser(@RequestBody @Valid User user) {
        return inMemoryUserStorage.updateUser(user);
    }

    @GetMapping(value = "/users")
    public Collection<User> getUsers() {
        return inMemoryUserStorage.getUsers();
    }

    @GetMapping(value = "/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable long userId) {
        return inMemoryUserStorage.getUserById(userId);
    }

    @PutMapping(value = "/users/{userId}/friends/{friendId}")
    public ResponseEntity<?> addFriend(@PathVariable long userId, @PathVariable long friendId) {
        return userService.addFriend(userId, friendId);
    }

    @GetMapping(value = "/users/{userId}/friends")
    public ResponseEntity<?> getUsersFriends(@PathVariable long userId) {
        return inMemoryUserStorage.getUsersFriends(userId);
    }

    @GetMapping(value = "/users/{userId}/friends/common/{friendId}")
    public ResponseEntity<?> getCommonFriends(@PathVariable long userId, @PathVariable long friendId) {
        return inMemoryUserStorage.getCommonFriends(userId, friendId);
    }

    @DeleteMapping(value = "/users/{userId}/friends/{friendId}")
    public ResponseEntity<?> deleteFriend(@PathVariable long userId, @PathVariable long friendId) {
        return userService.deleteFriend(userId,friendId);
    }
}

