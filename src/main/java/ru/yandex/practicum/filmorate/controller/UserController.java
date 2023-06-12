package ru.yandex.practicum.filmorate.controller;

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
        return userService.createUser(user);
    }

    @PutMapping(value = "/users")
    public ResponseEntity<?> updateUser(@RequestBody @Valid User user) {
        return userService.updateUser(user);
    }

    @GetMapping(value = "/users")
    public ResponseEntity<?> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(value = "/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable long userId) {
        return userService.getUserById(userId);
    }

    @PutMapping(value = "/users/{userId}/friends/{friendId}")
    public ResponseEntity<?> addFriend(@PathVariable long userId, @PathVariable long friendId) {
        return userService.addFriend(userId, friendId);
    }

    @GetMapping(value = "/users/{userId}/friends")
    public ResponseEntity<?> getUsersFriends(@PathVariable long userId) {
        return userService.getUsersFriends(userId);
    }

    @GetMapping(value = "/users/{userId}/friends/common/{friendId}")
    public ResponseEntity<?> getCommonFriends(@PathVariable long userId, @PathVariable long friendId) {
        return userService.getCommonFriends(userId, friendId);
    }

    @DeleteMapping(value = "/users/{userId}/friends/{friendId}")
    public ResponseEntity<?> deleteFriend(@PathVariable long userId, @PathVariable long friendId) {
        return userService.deleteFriend(userId, friendId);
    }
}