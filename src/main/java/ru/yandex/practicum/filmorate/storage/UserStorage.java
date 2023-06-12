package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;
import java.util.Set;

public interface UserStorage {
    void addUser(User user);

    void updateUser(User user);

    Map<Long, User> getUsers();

    void addFriendId(Long userID, Long friendID);

    void deleteFriendId(Long userID, Long friendID);

    Map<Long, Set<Long>> getUsersFriendsIds();
}
