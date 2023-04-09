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
    private final Map<Long, Set<Long>> friendsListHashMap = new HashMap<>();
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
    public ResponseEntity<?> getUserById(@PathVariable long userId) {
        boolean isUserAdded = false;
        for (User user : userStorage.getUsers()) {
            if (user.getId() == userId) {
                isUserAdded = true;
                break;
            }
        }
        if (!isUserAdded) {
            log.warn("Incorrect ID of user");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        } else {
            log.debug("Get User");
            return new ResponseEntity<>(userStorage.getUsers().stream().filter(x -> x.getId() == userId).findFirst(), HttpStatus.OK);
        }
    }

    @PutMapping(value = "/users/{userId}/friends/{friendId}")
    public ResponseEntity<?> addFriend(@PathVariable long userId, @PathVariable long friendId) {
        boolean isUserAccountAdded = false;
        boolean isFriendAccountAdded = false;
        Long usersId = null;
        Long friendsId = null;

        for (User user : userStorage.getUsers()) {
            if (user.getId() == userId) {
                isUserAccountAdded = true;
                usersId = user.getId();
            }
        }

        for (User friend : userStorage.getUsers()) {
            if (friend.getId() == friendId) {
                isFriendAccountAdded = true;
                friendsId = friend.getId();
            }
        }

        if (!isUserAccountAdded || !isFriendAccountAdded) {
            log.warn("Incorrect ID of user");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        }

        if (!friendsListHashMap.containsKey(userId)) {
            Set<Long> friendsIds = new TreeSet<>();
            friendsListHashMap.put(userId, friendsIds);
        }
        if (!friendsListHashMap.containsKey(friendId)) {
            Set<Long> friendsIds = new TreeSet<>();
            friendsListHashMap.put(friendId, friendsIds);
        }

        friendsListHashMap.get(userId).add(friendsId);
        friendsListHashMap.get(friendId).add(usersId);
        log.debug("Friend added");
        return new ResponseEntity<>("friend added", HttpStatus.OK);
    }

    @GetMapping(value = "/users/{userId}/friends")
    public ResponseEntity<?> getUsersFriends(@PathVariable long userId) {
        if (friendsListHashMap.containsKey(userId)) {
            List<User> friends = new ArrayList<>();
            for (User user : userStorage.getUsers()) {
                if (friendsListHashMap.get(userId).contains(user.getId())) {
                    friends.add(user);
                }
            }
            log.debug("Get users friends list");
            return new ResponseEntity<>(friends, HttpStatus.OK);
        } else {
            log.warn("Incorrect ID of user");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/users/{userId}/friends/common/{friendId}")
    public ResponseEntity<?> getCommonFriends(@PathVariable long userId, @PathVariable long friendId) {
        List<User> commonFriendsList = new ArrayList<>();
        boolean isUserAccountAdded = false;
        boolean isFriendAccountAdded = false;

        for (User user : userStorage.getUsers()) {
            if (user.getId() == userId) {
                isUserAccountAdded = true;
                break;
            }
        }
        for (User friend : userStorage.getUsers()) {
            if (friend.getId() == friendId) {
                isFriendAccountAdded = true;
                break;
            }
        }
        if (!isUserAccountAdded || !isFriendAccountAdded) {
            log.warn("Incorrect ID of user");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        }

        try {
            for (User user : userStorage.getUsers()) {
                if (friendsListHashMap.get(userId).contains(user.getId()) && friendsListHashMap.get(friendId).contains(user.getId())) {
                    commonFriendsList.add(user);
                }
            }
        } catch (NullPointerException e) {
            log.warn("Empty common friends list");
            return new ResponseEntity<>(commonFriendsList, HttpStatus.OK);
        }
        return new ResponseEntity<>(commonFriendsList, HttpStatus.OK);
    }

    @DeleteMapping(value = "/users/{userId}/friends/{friendId}")
    public ResponseEntity<?> deleteFriend(@PathVariable long userId, @PathVariable long friendId) {
        friendsListHashMap.get(userId).remove(friendId);
        friendsListHashMap.get(friendId).remove(userId);
        log.debug("Friend deleted");
        return new ResponseEntity<>("Friend deleted", HttpStatus.OK);
    }
}