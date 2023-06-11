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

import java.util.ArrayList;
import java.util.List;

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

    public ResponseEntity<?> createUser(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        inMemoryUserStorage.addUser(user);
        log.debug("User added: {}", user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity<?> updateUser(User user) {
        if (inMemoryUserStorage.getUsers().containsKey(user.getId())) {
            inMemoryUserStorage.updateUser(user);
            log.debug("User Updated :{}", user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            log.warn("User ID not found");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getAllUsers() {
        log.debug("Get All Users :{}", inMemoryUserStorage.getUsers().values());
        return new ResponseEntity<>(inMemoryUserStorage.getUsers().values(), HttpStatus.OK);
    }

    public ResponseEntity<?> getUserById(long userId) {
        if (inMemoryUserStorage.getUsers().containsKey(userId)) {
            log.debug("Get User By ID :{}", inMemoryUserStorage.getUsers().get(userId));
            return new ResponseEntity<>(inMemoryUserStorage.getUsers().get(userId), HttpStatus.OK);
        } else {
            log.warn("User ID not found");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> addFriend(long userId, long friendId) {
        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
            log.warn("Incorrect ID of user");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        }
        if (!inMemoryUserStorage.getUsers().containsKey(friendId)) {
            log.warn("Incorrect ID of friend");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("Friend ID not found")), HttpStatus.NOT_FOUND);
        }
        inMemoryUserStorage.addFriend(inMemoryUserStorage.getUsers().get(userId), inMemoryUserStorage.getUsers().get(friendId));
        inMemoryUserStorage.addFriend(inMemoryUserStorage.getUsers().get(friendId), inMemoryUserStorage.getUsers().get(userId));
        log.debug("Friend added");
        return new ResponseEntity<>("friend added", HttpStatus.OK);
    }

    public ResponseEntity<?> getUsersFriends(long userId) {
        if (inMemoryUserStorage.getUsers().containsKey(userId)) {
            List<User> usersFriends = new ArrayList<>();
            for (Long friendsId : inMemoryUserStorage.getUsersFriendsIDS().get(userId)) {
                usersFriends.add(inMemoryUserStorage.getUsers().get(friendsId));
            }
            log.debug("Get Users Friends :{}", usersFriends);
            return new ResponseEntity<>(usersFriends, HttpStatus.OK);
        } else {
            log.warn("Incorrect ID of user");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        }
    }

//    public ResponseEntity<?> getCommonFriends(long userId, long friendId) {
//        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
//            log.warn("Incorrect ID of user");
//            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
//        }
//        if (!inMemoryUserStorage.getUsers().containsKey(friendId)) {
//            log.warn("Incorrect ID of friend");
//            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("Friend ID not found")), HttpStatus.NOT_FOUND);
//        }
//        List<User> commonFriendsList = new ArrayList<>();
//        try {
//            for (User friend : inMemoryUserStorage.getUsersFriendsMap().get(inMemoryUserStorage.getUsers().get(userId))) {
//                if (inMemoryUserStorage.getUsersFriendsMap().get(inMemoryUserStorage.getUsers().get(friendId)).contains(friend)) {
//                    commonFriendsList.add(friend);
//                }
//            }
//            log.debug("Get common friends list:{}", commonFriendsList);
//            return new ResponseEntity<>(commonFriendsList, HttpStatus.OK);
//        } catch (NullPointerException e) {
//            return new ResponseEntity<>(commonFriendsList, HttpStatus.OK);
//        }
//    }

//    public ResponseEntity<?> deleteFriend(long userId, long friendId) {
//        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
//            log.warn("Incorrect ID of user");
//            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
//        }
//        if (!inMemoryUserStorage.getUsers().containsKey(friendId)) {
//            log.warn("Incorrect ID of friend");
//            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("Friend ID not found")), HttpStatus.NOT_FOUND);
//        }
//        if (!inMemoryUserStorage.getUsersFriendsMap().containsKey(inMemoryUserStorage.getUsers().get(userId))) {
//            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("Not found friend in friendList")), HttpStatus.NOT_FOUND);
//        }
//        inMemoryUserStorage.getUsersFriendsMap().get(inMemoryUserStorage.getUsers().get(userId)).remove(inMemoryUserStorage.getUsers().get(friendId));
//        inMemoryUserStorage.getUsersFriendsMap().get(inMemoryUserStorage.getUsers().get(friendId)).remove(inMemoryUserStorage.getUsers().get(userId));
//        log.debug("Friend deleted");
//        return new ResponseEntity<>("Friend deleted", HttpStatus.OK);
//    }
}