package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    void addUser(User user);

    Optional<User> findUserById(Long userId);

    List<User> findAllUsers();

    void updateUser(User user);

    void addFriendId(Long userID, Long friendID);

    List<User> findUsersFriends(Long userId);

    List<User> findCommonFriends(Long userId, Long friendId);

    void deleteFriendId(Long userID, Long friendID);

    boolean isUsersFriend(Long userId, Long friendId);
}
