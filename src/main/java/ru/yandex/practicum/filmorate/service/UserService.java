package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ErrorException;
import ru.yandex.practicum.filmorate.exception.ErrorHandler;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final ErrorHandler errorHandler = new ErrorHandler();

    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public ResponseEntity<?> createUser(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        inMemoryUserStorage.addUser(user);
        log.debug("User Added: {}", user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity<?> updateUser(User user) {
        if (inMemoryUserStorage.getUsers().containsKey(user.getId())) {
            inMemoryUserStorage.updateUser(user);
            log.debug("User Updated :{}", user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            log.warn("User Id Not Found");
            return new ResponseEntity<>(errorHandler.handleException(new ErrorException("User Id Not Found")), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getAllUsers() {
        log.debug("Get All Users :{}", inMemoryUserStorage.getUsers().values());
        return new ResponseEntity<>(inMemoryUserStorage.getUsers().values(), HttpStatus.OK);
    }

    public ResponseEntity<?> getUserById(long userId) {
        if (inMemoryUserStorage.getUsers().containsKey(userId)) {
            log.debug("Get User By Id :{}", inMemoryUserStorage.getUsers().get(userId));
            return new ResponseEntity<>(inMemoryUserStorage.getUsers().get(userId), HttpStatus.OK);
        } else {
            log.warn("User Id Not Found");
            return new ResponseEntity<>(errorHandler.handleException(new ErrorException("User ID Not Found")), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> addFriend(long userId, long friendId) {
        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
            log.warn("Incorrect Id Of User");
            return new ResponseEntity<>(errorHandler.handleException(new ErrorException("User Id Not Found")), HttpStatus.NOT_FOUND);
        }
        if (!inMemoryUserStorage.getUsers().containsKey(friendId)) {
            log.warn("Incorrect Id Of Friend");
            return new ResponseEntity<>(errorHandler.handleException(new ErrorException("Friend Id Not Found")), HttpStatus.NOT_FOUND);
        }
        inMemoryUserStorage.addFriendId(userId, friendId);
        inMemoryUserStorage.addFriendId(friendId, userId);
        log.debug("Friend Added");
        return new ResponseEntity<>("Friend Added", HttpStatus.OK);
    }

    public ResponseEntity<?> getUsersFriends(long userId) {
        if (inMemoryUserStorage.getUsers().containsKey(userId)) {
            List<User> usersFriends = new ArrayList<>();
            for (Long friendsId : inMemoryUserStorage.getUsersFriendsIds().get(userId)) {
                usersFriends.add(inMemoryUserStorage.getUsers().get(friendsId));
            }
            log.debug("Get Users Friends :{}", usersFriends);
            return new ResponseEntity<>(usersFriends, HttpStatus.OK);
        } else {
            log.warn("Incorrect Id Of User");
            return new ResponseEntity<>(errorHandler.handleException(new ErrorException("User Id Not Found")), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getCommonFriends(long userId, long friendId) {
        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
            log.warn("Incorrect Id Of User");
            return new ResponseEntity<>(errorHandler.handleException(new ErrorException("User Id Not Found")), HttpStatus.NOT_FOUND);
        }
        if (!inMemoryUserStorage.getUsers().containsKey(friendId)) {
            log.warn("Incorrect Id Of Friend");
            return new ResponseEntity<>(errorHandler.handleException(new ErrorException("Friend Id Not Found")), HttpStatus.NOT_FOUND);
        }
        List<User> commonFriendsList = new ArrayList<>();
        try {
            for (Long friendID : inMemoryUserStorage.getUsersFriendsIds().get(userId)) {
                if (inMemoryUserStorage.getUsersFriendsIds().get(friendId).contains(friendID)) {
                    commonFriendsList.add(inMemoryUserStorage.getUsers().get(friendID));
                }
            }
            log.debug("Get Common Friends List:{}", commonFriendsList);
            return new ResponseEntity<>(commonFriendsList, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(commonFriendsList, HttpStatus.OK);
        }
    }

    public ResponseEntity<?> deleteFriend(long userId, long friendId) {
        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
            log.warn("Incorrect Id Of User");
            return new ResponseEntity<>(errorHandler.handleException(new ErrorException("User Id Not Found")), HttpStatus.NOT_FOUND);
        }
        if (!inMemoryUserStorage.getUsers().containsKey(friendId)) {
            log.warn("Incorrect Id Of Friend");
            return new ResponseEntity<>(errorHandler.handleException(new ErrorException("Friend Id Not Found")), HttpStatus.NOT_FOUND);
        }
        if (!inMemoryUserStorage.getUsersFriendsIds().get(userId).contains(friendId)) {
            return new ResponseEntity<>(errorHandler.handleException(new ErrorException("Not Found Friend In FriendList")), HttpStatus.NOT_FOUND);
        }
        inMemoryUserStorage.deleteFriendId(userId, friendId);
        inMemoryUserStorage.deleteFriendId(friendId, userId);
        log.debug("Friend Deleted");
        return new ResponseEntity<>("Friend Deleted", HttpStatus.OK);
    }
}