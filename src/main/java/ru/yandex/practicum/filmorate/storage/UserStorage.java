package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;
import java.util.Set;

public interface UserStorage {
    void addUser(User user);

    void updateUser(User user);

    Map<Long, User> getUsers();

    Map<User, Set<User>> getUsersFriendsMap();

    void addFriend(User user, User friend);
}
