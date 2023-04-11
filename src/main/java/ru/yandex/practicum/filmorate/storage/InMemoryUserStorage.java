package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
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

    public User getUserById(long id) {
        return users.get(id);
    }

    public Map<Long, User> getAllUsers() {
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

    public Set<User> getUserFriendList(User user) {
        return usersFriends.get(user);
    }

    public List<User> getCommonListOfFriends(User user, User usersFriend) {
        List<User> commonFriendsList = new ArrayList<>();
        try {
            for (User friend : usersFriends.get(user)) {
                if (usersFriends.get(usersFriend).contains(friend)) {
                    commonFriendsList.add(friend);
                }
            }
            return commonFriendsList;
        } catch (NullPointerException e) {
            return commonFriendsList;
        }
    }

    public void deleteFriend(User user, User friend) {
        usersFriends.get(user).remove(friend);
        usersFriends.get(friend).remove(user);
    }
}
