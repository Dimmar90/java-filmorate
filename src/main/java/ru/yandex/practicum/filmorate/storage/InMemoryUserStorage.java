package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ErrorException;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<User, Deque<User>> usersFriends = new HashMap<>();

    private long id = 0;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @ExceptionHandler(value = ErrorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleWrongUserUpdateException(ErrorException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    public User addUser(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(++id);
        users.put(user.getId(), user);
        log.debug("User added: {}", user);
        return user;
    }

    public ResponseEntity<?> updateUser(User user) {
        long index = -1;
        if (users.containsKey(user.getId())) {
            index = user.getId();
        }
        if (index == -1) {
            log.warn("User ID not found");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        } else {
            users.put(user.getId(), user);
            log.debug("User update: {}", user.getName());
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    public Collection<User> getUsers() {
        log.debug("Get all users: {}", users.values());
        return users.values();
    }

    public Map<Long, User> getUsersMap() {
        return users;
    }

    public Map<User, Deque<User>> getUsersFriendsMap() {
        return usersFriends;
    }

    public ResponseEntity<?> getUserById(long userId) {
        if (users.containsKey(userId)) {
            log.debug("Get user by id: {}", users.get(userId));
            return new ResponseEntity<>(users.get(userId), HttpStatus.OK);
        } else {
            log.warn("Incorrect ID of user");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        }
    }

    public void addFriend(User user, User friend) {
        if (!usersFriends.containsKey(user)) {
            Deque<User> friends = new ArrayDeque<>();
            friends.addFirst(friend);
            usersFriends.put(user, friends);
        } else {
            usersFriends.get(user).addFirst(friend);
        }
    }

    public ResponseEntity<?> getUsersFriends(long userId) {
        if (users.containsKey(userId)) {
            log.debug("Get users friends list: {}", usersFriends.get(users.get(userId)));
            return new ResponseEntity<>(usersFriends.get(users.get(userId)), HttpStatus.OK);
        } else {
            log.warn("Incorrect ID of user");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getCommonFriends(long userId, long friendId) {
        if (!users.containsKey(userId)) {
            log.warn("Incorrect ID of user");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("User ID not found")), HttpStatus.NOT_FOUND);
        }
        if (!users.containsKey(friendId)) {
            log.warn("Incorrect ID of friend");
            return new ResponseEntity<>(handleWrongUserUpdateException(new ErrorException("Friend ID not found")), HttpStatus.NOT_FOUND);
        }
        List<User> commonFriendsList = new ArrayList<>();
        try {
            for (User friend : usersFriends.get(users.get(userId))) {
                if (usersFriends.get(users.get(friendId)).contains(friend)) {
                    commonFriendsList.add(friend);
                }
            }
            log.debug("Get common friends list:{}", commonFriendsList);
            return new ResponseEntity<>(commonFriendsList, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(commonFriendsList, HttpStatus.OK);
        }
    }
}