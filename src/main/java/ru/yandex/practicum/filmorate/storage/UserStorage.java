package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Deque;
import java.util.Map;

public interface UserStorage {
    User addUser(User user);

    ResponseEntity<?> updateUser(User user);

    Collection<User> getUsers();

    Map<Long, User> getUsersMap();

    Map<User, Deque<User>> getUsersFriendsMap();

    ResponseEntity<?> getUserById(long userId);

    ResponseEntity<?> getUsersFriends(long userId);

    ResponseEntity<?> getCommonFriends(long userId, long friendId);

    void addFriend(User user, User friend);
}
