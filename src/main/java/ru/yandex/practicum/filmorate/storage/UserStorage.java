package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserWithStatus;

import java.util.List;

public interface UserStorage {

    void addUser(User user);

    User findUserById(Long userId);

    List<User> findAllUsers();

    void updateUser(User user);

    void addFriend(Long userID, Long friendID);

    List<UserWithStatus> findUsersFriends(Long userId);

    List<User> findCommonFriends(Long userId, Long friendId);

    void deleteFriendId(Long userID, Long friendID);
}
