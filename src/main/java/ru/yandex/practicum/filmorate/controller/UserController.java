package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/users")
    public ResponseEntity<?> createUser(@RequestBody @Valid User user) {
        userService.createUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping(value = "/users")
    public ResponseEntity<?> updateUser(@RequestBody @Valid User user) {
        userService.updateUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(value = "/users")
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping(value = "/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable long userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @PutMapping(value = "/users/{userId}/friends/{friendId}")
    public ResponseEntity<?> addFriend(@PathVariable long userId, @PathVariable long friendId) {
        userService.addFriend(userId, friendId);
        return new ResponseEntity<>("Friend Added", HttpStatus.OK);
    }

    @GetMapping(value = "/users/{userId}/friends")
    public ResponseEntity<?> getUsersFriends(@PathVariable long userId) {
        return new ResponseEntity<>(userService.getUsersFriends(userId), HttpStatus.OK);
    }

    @GetMapping(value = "/users/{userId}/friends/common/{friendId}")
    public ResponseEntity<?> getCommonFriends(@PathVariable long userId, @PathVariable long friendId) {
        return new ResponseEntity<>(userService.getCommonFriends(userId, friendId), HttpStatus.OK);
    }

    @DeleteMapping(value = "/users/{userId}/friends/{friendId}")
    public ResponseEntity<?> deleteFriend(@PathVariable long userId, @PathVariable long friendId) {
        userService.deleteFriend(userId, friendId);
        return new ResponseEntity<>("Friend Deleted", HttpStatus.OK);
    }
}