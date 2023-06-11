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
    private final Map<User, Set<User>> usersFriends = new HashMap<>();

    private long id = 0;


    @ExceptionHandler(value = ErrorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleWrongUserUpdateException(ErrorException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    public void addUser(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
    }

    public void updateUser(User user) {
        users.put(user.getId(), user);
    }

    public Map<Long, User> getUsers() {
        return users;
    }

    public void addFriend(User user, User friend) {
        if (!usersFriends.containsKey(user)) {
            Set<User> friends = new HashSet<>();
            friends.add(friend);
            usersFriends.put(user, friends);
        } else {
            usersFriends.get(user).add(friend);
        }
    }

    public Map<User, Set<User>> getUsersFriendsMap() {
        return usersFriends;
    }
}