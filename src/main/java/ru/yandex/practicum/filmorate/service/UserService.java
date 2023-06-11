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
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

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

    public ResponseEntity<?> addFriend(long userId, long friendId) {
        if (!inMemoryUserStorage.getUsersMap().containsKey(userId)) {
            log.warn("Incorrect ID of user");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        }
        if (!inMemoryUserStorage.getUsersMap().containsKey(friendId)) {
            log.warn("Incorrect ID of friend");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("Friend ID not found")), HttpStatus.NOT_FOUND);
        }
        inMemoryUserStorage.addFriend(inMemoryUserStorage.getUsersMap().get(userId), inMemoryUserStorage.getUsersMap().get(friendId));
        inMemoryUserStorage.addFriend(inMemoryUserStorage.getUsersMap().get(friendId), inMemoryUserStorage.getUsersMap().get(userId));
        log.debug("Friend added");
        return new ResponseEntity<>("friend added", HttpStatus.OK);
    }

    public ResponseEntity<?> deleteFriend(long userId, long friendId) {
        if (!inMemoryUserStorage.getUsersMap().containsKey(userId)) {
            log.warn("Incorrect ID of user");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        }
        if (!inMemoryUserStorage.getUsersMap().containsKey(friendId)) {
            log.warn("Incorrect ID of friend");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("Friend ID not found")), HttpStatus.NOT_FOUND);
        }
        if (!inMemoryUserStorage.getUsersFriendsMap().containsKey(inMemoryUserStorage.getUsersMap().get(userId))) {
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("Not found friend in friendList")), HttpStatus.NOT_FOUND);
        }
        inMemoryUserStorage.getUsersFriendsMap().get(inMemoryUserStorage.getUsersMap().get(userId)).remove(inMemoryUserStorage.getUsersMap().get(friendId));
        inMemoryUserStorage.getUsersFriendsMap().get(inMemoryUserStorage.getUsersMap().get(friendId)).remove(inMemoryUserStorage.getUsersMap().get(userId));
        log.debug("Friend deleted");
        return new ResponseEntity<>("Friend deleted", HttpStatus.OK);
    }
}