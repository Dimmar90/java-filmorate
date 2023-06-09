package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exception.ErrorException;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;

@Service
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @ExceptionHandler(value = ErrorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleWrongUserUpdateException(ErrorException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User createUser(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        inMemoryUserStorage.addUser(user);
        return user;
    }

    public ResponseEntity<?> updateUser(User user) {
        long index = -1;
        if (inMemoryUserStorage.getAllUsers().containsKey(user.getId())) {
            index = user.getId();
        }
        if (index == -1) {
            log.warn("User ID not found");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        } else {
            inMemoryUserStorage.updateUser(user);
            log.debug("User update: {}", user.getName());
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    public Collection<User> getUsers() {
        log.debug("Get all users: {}", inMemoryUserStorage.getAllUsers().values());
        return inMemoryUserStorage.getAllUsers().values();
    }

    public ResponseEntity<?> getUserById(long userId) {
        if (inMemoryUserStorage.getAllUsers().containsKey(userId)) {
            log.debug("Get user by id: {}", userId);
            return new ResponseEntity<>(inMemoryUserStorage.getUserById(userId), HttpStatus.OK);
        } else {
            log.warn("Incorrect ID of user");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> addFriend(long userId, long friendId) {
        if (!inMemoryUserStorage.getAllUsers().containsKey(userId)) {
            log.warn("Incorrect ID of user");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        }
        if (!inMemoryUserStorage.getAllUsers().containsKey(friendId)) {
            log.warn("Incorrect ID of friend");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("Friend ID not found")), HttpStatus.NOT_FOUND);
        }
        inMemoryUserStorage.addFriend(inMemoryUserStorage.getUserById(userId), inMemoryUserStorage.getUserById(friendId));
        inMemoryUserStorage.addFriend(inMemoryUserStorage.getUserById(friendId), inMemoryUserStorage.getUserById(userId));
        log.debug("Friend added");
        return new ResponseEntity<>("friend added", HttpStatus.OK);
    }

    public ResponseEntity<?> getUsersFriends(long userId) {
        if (inMemoryUserStorage.getAllUsers().containsKey(userId)) {
            log.debug("Get users friends list");
            return new ResponseEntity<>(inMemoryUserStorage.getUserFriendList(inMemoryUserStorage.getUserById(userId)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getCommonFriends(long userId, long friendId) {
        if (!inMemoryUserStorage.getAllUsers().containsKey(userId)) {
            log.warn("Incorrect ID of user");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        }
        if (!inMemoryUserStorage.getAllUsers().containsKey(friendId)) {
            log.warn("Incorrect ID of friend");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("Friend ID not found")), HttpStatus.NOT_FOUND);
        }
        log.debug("Get common friends list");
        return new ResponseEntity<>(inMemoryUserStorage.getCommonListOfFriends(inMemoryUserStorage.getUserById(userId),
                inMemoryUserStorage.getUserById(friendId)), HttpStatus.OK);
    }

    public ResponseEntity<?> deleteFriend(long userId, long friendId) {
        if (!inMemoryUserStorage.getAllUsers().containsKey(userId)) {
            log.warn("Incorrect ID of user");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        }
        if (!inMemoryUserStorage.getAllUsers().containsKey(friendId)) {
            log.warn("Incorrect ID of friend");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("Friend ID not found")), HttpStatus.NOT_FOUND);
        }
        if (!inMemoryUserStorage.getUserFriendList(inMemoryUserStorage.getUserById(userId)).contains(inMemoryUserStorage.getUserById(friendId))) {
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("Not found friend in friendList")), HttpStatus.NOT_FOUND);
        }
        inMemoryUserStorage.deleteFriend(inMemoryUserStorage.getUserById(userId), inMemoryUserStorage.getUserById(friendId));
        log.debug("Friend deleted");
        return new ResponseEntity<>("Friend deleted", HttpStatus.OK);
    }
}