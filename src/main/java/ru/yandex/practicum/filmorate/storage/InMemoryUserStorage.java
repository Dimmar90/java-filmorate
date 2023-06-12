package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exception.ErrorException;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Set<Long>> usersFriendsIDS = new HashMap<>();
    private long id = 0;

    @ExceptionHandler(value = ErrorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleWrongUserUpdateException(ErrorException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @Override
    public void addUser(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
    }

    @Override
    public void updateUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public Map<Long, User> getUsers() {
        return users;
    }

    @Override
    public void addFriendId(Long userID, Long friendID) {
        if (!usersFriendsIDS.containsKey(userID)) {
            Set<Long> friendsIDs = new HashSet<>();
            friendsIDs.add(friendID);
            usersFriendsIDS.put(userID, friendsIDs);
        } else {
            usersFriendsIDS.get(userID).add(friendID);
        }
    }

    @Override
    public void deleteFriendId(Long userID, Long friendID) {
        usersFriendsIDS.get(userID).remove(friendID);
    }

    @Override
    public Map<Long, Set<Long>> getUsersFriendsIds() {
        return usersFriendsIDS;
    }
}