package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Deque;
import java.util.Map;

public interface UserStorage {
    void addUser(User user);

    void updateUser(User user);

    Map<Long, User> getUsers();

    Map<User, Deque<User>> getUsersFriendsMap();

    void addFriend(User user, User friend);
}
