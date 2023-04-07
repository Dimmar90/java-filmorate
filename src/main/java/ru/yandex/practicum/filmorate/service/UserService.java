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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@Component
@RestController
public class UserService {
    UserStorage userStorage;
    private final Map<Integer, Set<User>> friendsListHashMap = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @ExceptionHandler(value = ErrorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleWrongUserUpdateException(ErrorException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @GetMapping(value = "/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable int userId) {
        boolean isUserAdded = false;
        for (User user : userStorage.getUsers()) {
            if (user.getId() == userId) {
                isUserAdded = true;
                break;
            }
        }
        if (!isUserAdded) {
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(userStorage.getUsers().stream().filter(x -> x.getId() == userId).findFirst(), HttpStatus.OK);
        }
    }

    @PutMapping(value = "/users/{userId}/friends/{friendId}")
    public ResponseEntity<?> addFriend(@PathVariable int userId, @PathVariable int friendId) {

        boolean isUserAccountAdded = false;
        boolean isFriendAccountAdded = false;
        User userToAdd = null;
        User friendToAdd = null;

        for (User user : userStorage.getUsers()) {
            if (user.getId() == userId) {
                isUserAccountAdded = true;
                userToAdd = user;
            }
        }

        for (User friend : userStorage.getUsers()) {
            if (friend.getId() == friendId) {
                isFriendAccountAdded = true;
                friendToAdd = friend;
            }
        }

        if (!isUserAccountAdded || !isFriendAccountAdded) {
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        }

        if (!friendsListHashMap.containsKey(userId)) {
            Set<User> friends = new HashSet<>();
            friendsListHashMap.put(userId, friends);
        }
        if (!friendsListHashMap.containsKey(friendId)) {
            Set<User> friends = new HashSet<>();
            friendsListHashMap.put(friendId, friends);
        }
        friendsListHashMap.get(userId).add(friendToAdd);
        friendsListHashMap.get(friendId).add(userToAdd);
        log.debug("Friend added");
        return new ResponseEntity<>("friend added", HttpStatus.OK);
    }

    @GetMapping(value = "/users/{userId}/friends")
    public ResponseEntity<?> getUsersFriends(@PathVariable int userId) {
        if (!friendsListHashMap.containsKey(userId)) {
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(friendsListHashMap.get(userId), HttpStatus.OK);
        }
    }
}